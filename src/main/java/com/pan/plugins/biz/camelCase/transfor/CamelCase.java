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
        // �ϸ������Ƿ�Ϊ��������
        boolean lastNormalWord = true;
        for (int i = 0; i < wordList.size(); i++) {
            Word word = wordList.get(i);
            // ��ǰ����Ϊ����
            if (Word.Type.SYMBOL.equals(word.getType())) {
                word.setCase(false);
                word.setCurPosType(Word.PosType.SYMBOL);
                word.setTarPosType(Word.PosType.SYMBOL);
                // �����У�ֻ����������������
                lastNormalWord = word.getText().equals(String.valueOf(Constants.QUOTES));
            } else if (Word.Type.WORD.equals(word.getType())) {
                if (Constants.isRetainWord(word.getText())) {
                    word.setCase(false);
                    word.setCurPosType(Word.PosType.RETAIN);
                    word.setTarPosType(Word.PosType.RETAIN);
                    // ����
                    lastNormalWord = false;
                } else {
                    if (lastNormalWord) {
                        boolean underLineMark = word.getText().contains(String.valueOf(Constants.UNDER_LINE_MARK));
                        word.setCase(true);
                        word.setCurPosType(underLineMark ? Word.PosType.UNDER_LINE_SPILT : Word.PosType.CAMEL);
                        // Ŀ�������뵱ǰ���ͶԵ�
                        word.setTarPosType(underLineMark ? Word.PosType.CAMEL : Word.PosType.UNDER_LINE_SPILT);
                        // ����
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
     * ���ı�ת�����
     * �ų���Ӧת�����ı�
     * eg:
     * 1��ǰ���� ˫���� ���ı�ӦΪ UNDER_LINE_SPILT ��ʽ
     * 2��ǰ���� ˫���� ���ı�ӦΪ CAMEL ��ʽ
     */
    private void longTextCheckCase() {
        // �����������账��
        if (wordList.size() > 1) {
            for (int i = 0; i < wordList.size(); i++) {
                // ��������ת���ĵ���
                if (!wordList.get(i).isCase()) {
                    continue;
                }
                // ��Ҫת���ĵ��ʴ���
                int pre = Math.max(0, i - 1);
                int next = Math.min(wordList.size() - 1, i + 1);
                // �Ƿ�ǰ��������
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
