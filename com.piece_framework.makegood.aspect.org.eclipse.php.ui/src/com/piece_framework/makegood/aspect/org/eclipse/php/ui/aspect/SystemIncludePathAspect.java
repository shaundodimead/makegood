/**
 * Copyright (c) 2009 MATSUFUJI Hideharu <matsufuji2008@gmail.com>,
 *               2010-2011 KUBO Atsuhiro <kubo@iteman.jp>,
 * All rights reserved.
 *
 * This file is part of MakeGood.
 *
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package com.piece_framework.makegood.aspect.org.eclipse.php.ui.aspect;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;

import com.piece_framework.makegood.aspect.Aspect;

/**
 * @deprecated Deprecated since version 3.2, to be removed in 4.0.
 */
public class SystemIncludePathAspect extends Aspect {
    private static final String JOINPOINT_GETCPLISTELEMENTTEXT_INSERTBEFORE =
        "PHPIPListLabelProvider#getCPListElementText() [insert before]"; //$NON-NLS-1$
    private static final String JOINPOINT_GETCPLISTELEMENTTEXT_ADDMETHOD =
        "PHPIPListLabelProvider#getCPListElementText() [add method]"; //$NON-NLS-1$
    private static final String JOINPOINT_GETCPLISTELEMENTBASEIMAGE_INSERTBEFORE =
        "PHPIPListLabelProvider#getCPListElementBaseImage() [insert before]"; //$NON-NLS-1$
    private static final String JOINPOINT_CREATECONTROL_NEW_PHPINCLUDEPATHSOURCEPAGE =
        "PHPIncludePathsBlock#createControl() [new PHPIncludePathSourcePage]"; //$NON-NLS-1$
    private static final String[] JOINPOINTS = {
        JOINPOINT_GETCPLISTELEMENTTEXT_INSERTBEFORE,
        JOINPOINT_GETCPLISTELEMENTTEXT_ADDMETHOD,
        JOINPOINT_GETCPLISTELEMENTBASEIMAGE_INSERTBEFORE,
        JOINPOINT_CREATECONTROL_NEW_PHPINCLUDEPATHSOURCEPAGE
    };
    private static final String WEAVINGCLASS_PHPIPLISTLABELPROVIDER =
        "org.eclipse.php.internal.ui.preferences.includepath.PHPIPListLabelProvider"; //$NON-NLS-1$
    private static final String WEAVINGCLASS_PHPINCLUDEPATHSBLOCK =
        "org.eclipse.php.internal.ui.preferences.includepath.PHPIncludePathsBlock"; //$NON-NLS-1$
    private static final String[] WEAVINGCLASSES = {
        WEAVINGCLASS_PHPIPLISTLABELPROVIDER,
        WEAVINGCLASS_PHPINCLUDEPATHSBLOCK
    };

    @Override
    protected void doWeave() throws NotFoundException, CannotCompileException {
        CtClass weavingClass1 = ClassPool.getDefault().get(WEAVINGCLASS_PHPIPLISTLABELPROVIDER);
        editGetCPListElementTextMethod(weavingClass1);
        editGetCPListElementBaseImageMethod(weavingClass1);
        markClassAsWoven(weavingClass1);

        CtClass weavingClass2 = ClassPool.getDefault().get(WEAVINGCLASS_PHPINCLUDEPATHSBLOCK);
        editCreateControlMethod(weavingClass2);
        markClassAsWoven(weavingClass2);
    }

    private void editGetCPListElementTextMethod(CtClass weavingClass) throws CannotCompileException, NotFoundException {
        weavingClass.getDeclaredMethod("getCPListElementText").insertBefore( //$NON-NLS-1$
"org.eclipse.core.resources.IResource target = cpentry.getResource();" + //$NON-NLS-1$
"if (target != null) {" + //$NON-NLS-1$
"    com.piece_framework.makegood.includepath.ConfigurationIncludePath configuration = new com.piece_framework.makegood.includepath.ConfigurationIncludePath(target.getProject());" + //$NON-NLS-1$
"    if (configuration.equalsDummyResource(target)) {" + //$NON-NLS-1$
"        return com.piece_framework.makegood.includepath.ConfigurationIncludePath.text;" + //$NON-NLS-1$
"    }" + //$NON-NLS-1$
"}" //$NON-NLS-1$
        );

        markJoinPointAsPassed(JOINPOINT_GETCPLISTELEMENTTEXT_INSERTBEFORE);
        markJoinPointAsPassed(JOINPOINT_GETCPLISTELEMENTTEXT_ADDMETHOD);
    }

    private void editGetCPListElementBaseImageMethod(CtClass weavingClass) throws CannotCompileException, NotFoundException {
        weavingClass.getDeclaredMethod("getCPListElementBaseImage").insertBefore( //$NON-NLS-1$
"org.eclipse.core.resources.IResource target = cpentry.getResource();" + //$NON-NLS-1$
"if (target != null) {" + //$NON-NLS-1$
"    com.piece_framework.makegood.includepath.ConfigurationIncludePath configuration = new com.piece_framework.makegood.includepath.ConfigurationIncludePath(target.getProject());" + //$NON-NLS-1$
"    if (configuration.equalsDummyResource(target)) {" + //$NON-NLS-1$
"        return com.piece_framework.makegood.includepath.ConfigurationIncludePath.icon;" + //$NON-NLS-1$
"    }" + //$NON-NLS-1$
"}" //$NON-NLS-1$
        );

        markJoinPointAsPassed(JOINPOINT_GETCPLISTELEMENTBASEIMAGE_INSERTBEFORE);
    }

    private void editCreateControlMethod(CtClass weavingClass) throws NotFoundException, CannotCompileException {
        weavingClass.getDeclaredMethod("createControl").instrument( //$NON-NLS-1$
            new ExprEditor() {
                @Override
                public void edit(NewExpr expression) throws CannotCompileException {
                    if (expression.getClassName().equals("org.eclipse.php.internal.ui.preferences.includepath.PHPIncludePathSourcePage")) { //$NON-NLS-1$
                        expression.replace(
"$_ = new com.piece_framework.makegood.aspect.org.eclipse.php.ui.aspect.PHPIncludePathSourcePageForConfiguration($1);" //$NON-NLS-1$
                        );

                        markJoinPointAsPassed(JOINPOINT_CREATECONTROL_NEW_PHPINCLUDEPATHSOURCEPAGE);
                    }
                }
            }
        );
    }

    @Override
    protected String[] joinPoints() {
        return JOINPOINTS;
    }

    @Override
    protected String[] weavingClasses() {
        return WEAVINGCLASSES;
    }
}
