package com.pan.plugins.biz.camelCase.transfor;

import com.pan.plugins.base.BaseCase;
import com.pan.plugins.base.Constants;
import com.pan.plugins.biz.camelCase.base.Word;
import com.pan.plugins.util.CaseUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author panqj
 * @date 2023/05/31
 */

public class CamelCase extends BaseCase {

    private final String content;
    private List<Word> wordList = new ArrayList<>();

    private CamelCase(String content) {
        this.content = content;
    }

    public static CamelCase build(String content) {
        return new CamelCase(content);
    }

    @Override
    public CamelCase spilt() {
        wordList = CaseUtil.contextSplit(content);
        return this;
    }

    @Override
    public CamelCase checkCase() {
        // 上个单词是否为正常单词
        boolean lastNormalWord = true;
        for (int i = 0; i < wordList.size(); i++) {
            Word word = wordList.get(i);
            // 当前单词为符号
            if (Word.Type.SYMBOL.equals(word.getType())) {
                word.setCase(false);
                word.setCurPosType(Word.PosType.SYMBOL);
                word.setTarPosType(Word.PosType.SYMBOL);
                // 符号中，只有引号算正常单词
                lastNormalWord = word.getText().equals(String.valueOf(Constants.QUOTES));
            } else if (Word.Type.WORD.equals(word.getType())) {
                if (Constants.isRetainWord(word.getText())) {
                    word.setCase(false);
                    word.setCurPosType(Word.PosType.RETAIN);
                    word.setTarPosType(Word.PosType.RETAIN);
                    // 重置
                    lastNormalWord = false;
                } else {
                    if (lastNormalWord) {
                        boolean underLineMark = word.getText().contains(String.valueOf(Constants.UNDER_LINE_MARK));
                        word.setCase(true);
                        word.setCurPosType(underLineMark ? Word.PosType.UNDER_LINE_SPILT : Word.PosType.CAMEL);
                        // 目标类型与当前类型对调
                        word.setTarPosType(underLineMark ? Word.PosType.CAMEL : Word.PosType.UNDER_LINE_SPILT);
                        // 重置
                        lastNormalWord = false;
                    } else {
                        word.setCase(false);
                        word.setCurPosType(Word.PosType.NORMAL);
                        word.setTarPosType(Word.PosType.NORMAL);
                        lastNormalWord = true;
                    }
                }
            }
        }
        longTextCheckCase();
        return this;
    }

    /**
     * 长文本转换检查
     * 排除不应转换的文本
     * eg:
     * 1、前后有 双引号 的文本应为 UNDER_LINE_SPILT 格式
     * 2、前后无 双引号 的文本应为 CAMEL 格式
     */
    private void longTextCheckCase() {
        // 单个单词无需处理
        if (wordList.size() > 1) {
            for (int i = 0; i < wordList.size(); i++) {
                // 过滤无需转换的单词
                if (!wordList.get(i).isCase()) {
                    continue;
                }
                // 需要转换的单词处理
                int pre = Math.max(0, i - 1);
                int next = Math.min(wordList.size() - 1, i + 1);
                // 是否前后有引号
                boolean isTargetUnderLine = String.valueOf(Constants.QUOTES).equals(wordList.get(pre).getText()) ||
                        String.valueOf(Constants.QUOTES).equals(wordList.get(next).getText());
                if (isTargetUnderLine) {
                    wordList.get(i).setTarPosType(Word.PosType.UNDER_LINE_SPILT);
                } else {
                    wordList.get(i).setTarPosType(Word.PosType.CAMEL);
                }
            }
        }
    }

    @Override
    public CamelCase handleCase() {
        for (Word word : wordList) {
            if (word.isCase()) {
                String caseStr = Word.PosType.CAMEL.equals(word.getTarPosType()) ?
                        CaseUtil.caseCamel(word.getText()) : CaseUtil.caseNormal(word.getText());
                word.setText(caseStr);
            }
        }
        return this;
    }

    @Override
    public String toStr() {
        StringBuilder result = new StringBuilder();
        wordList.forEach(it -> result.append(it.getText()));
        return result.toString();
    }
}
