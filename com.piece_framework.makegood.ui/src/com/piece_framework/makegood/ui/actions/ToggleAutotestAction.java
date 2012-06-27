/**
 * Copyright (c) 2012 KUBO Atsuhiro <kubo@iteman.jp>,
 * All rights reserved.
 *
 * This file is part of MakeGood.
 *
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package com.piece_framework.makegood.ui.actions;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import com.piece_framework.makegood.core.AutotestScope;
import com.piece_framework.makegood.launch.RuntimeConfiguration;
import com.piece_framework.makegood.ui.views.ResultView;
import com.piece_framework.makegood.ui.views.ViewOpener;

/**
 * @since 2.1.0
 */
public abstract class ToggleAutotestAction implements IViewActionDelegate {
    private static final String[] ACTION_IDS = {
        RunAllTestsWhenFileIsSavedAction.ACTION_ID,
        RunLastTestWhenFileIsSavedAction.ACTION_ID,
        RunFailedTestsWhenFileIsSavedAction.ACTION_ID,
    };

    @Override
    public void init(IViewPart view) {
    }

    @Override
    public void run(IAction action) {
        ResultView resultView = (ResultView) ViewOpener.find(ResultView.VIEW_ID);
        if (resultView == null) return;

        for (String actionId: ACTION_IDS) {
            if (actionId.equals(action.getId()) == false) {
                ActionContributionItem item =
                        (ActionContributionItem) resultView.getViewSite().getActionBars().getToolBarManager().find(actionId);
                if (item != null) {
                    item.getAction().setChecked(false);
                }
            }
        }

        if (action.isChecked()) {
            RuntimeConfiguration.getInstance().setAutotestScope(getAutotestScope());
        } else {
            RuntimeConfiguration.getInstance().setAutotestScope(AutotestScope.NONE);
        }
    }

    @Override
    public void selectionChanged(IAction action, ISelection selection) {
    }

    protected abstract AutotestScope getAutotestScope();
}