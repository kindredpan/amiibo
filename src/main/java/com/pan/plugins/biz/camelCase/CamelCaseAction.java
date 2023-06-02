package com.pan.plugins.biz.camelCase;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.pan.plugins.biz.camelCase.transfor.CamelCase;

public class CamelCaseAction extends AnAction {


    @Override
    public void actionPerformed(AnActionEvent event) {
        Editor editor = event.getRequiredData(CommonDataKeys.EDITOR);
        SelectionModel selectionModel = editor.getSelectionModel();
        final String selectedText = selectionModel.getSelectedText();
        final String result;
        if (null != selectedText) {
            try {
                result = CamelCase.build(selectedText)
                        .spilt()
                        .checkCase()
                        .handleCase()
                        .toStr();

                int start = selectionModel.getSelectionStart();
                int end = selectionModel.getSelectionEnd();

                Project project = event.getRequiredData(CommonDataKeys.PROJECT);
                Document document = editor.getDocument();
                WriteCommandAction.runWriteCommandAction(project, () ->
                        document.replaceString(start, end, result)
                );
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }


}
