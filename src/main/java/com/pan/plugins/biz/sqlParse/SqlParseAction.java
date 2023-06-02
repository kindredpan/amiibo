package com.pan.plugins.biz.sqlParse;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.pan.plugins.base.R;
import com.pan.plugins.biz.sqlParse.transfor.SqlCase;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

public class SqlParseAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        Editor editor = event.getRequiredData(CommonDataKeys.EDITOR);
        CaretModel caretModel = editor.getCaretModel();
        String sqlText = getSysClipboardText();
        R r;
        if (null != sqlText && !"".equals(sqlText.trim())) {
            try {
                String result = SqlCase.build(sqlText)
                        .spilt()
                        .handleCase()
                        .toStr();
                r = R.ok("success", result);
            } catch (Exception e) {
                e.printStackTrace();
                r = R.fail("�Ƿ��� SQL �ı���\n" + sqlText);
            }
        } else {
            r = R.fail("Clipboard is empty!");
        }
        // �ɹ�����
        if (r.isSuccess()) {
            final String result = r.getResult().toString();
            int start = caretModel.getOffset();
            Project project = event.getRequiredData(CommonDataKeys.PROJECT);
            Document document = editor.getDocument();
            WriteCommandAction.runWriteCommandAction(project, () ->
                    document.insertString(start, result)
            );
        } else {
            Project project = event.getData(PlatformDataKeys.PROJECT);
            Messages.showMessageDialog(project, r.getMsg(), "SQL Parse Error", Messages.getErrorIcon());
        }

    }

    /**
     * ��ȡ���а��е�����
     *
     * @return
     */
    public static String getSysClipboardText() {
        String ret = "";
        Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
        // ��ȡ���а��е�����
        Transferable clipTf = sysClip.getContents(null);

        if (clipTf != null) {
            // ��������Ƿ����ı�����
            if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    ret = (String) clipTf
                            .getTransferData(DataFlavor.stringFlavor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    /**
     * ��ʽ������
     *
     * @param theElement
     */
    public static void reformatJavaFile(PsiElement theElement) {
        CodeStyleManager codeStyleManager = CodeStyleManager.getInstance(theElement.getProject());
        try {
            codeStyleManager.reformat(theElement);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
