/**
 * Copyright (c) 2010-2011, 2014-2015 KUBO Atsuhiro <kubo@iteman.jp>,
 * All rights reserved.
 *
 * This file is part of MakeGood.
 *
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package com.piece_framework.makegood.aspect.org.eclipse.php.debug.core;

import com.piece_framework.makegood.aspect.Aspect;
import com.piece_framework.makegood.aspect.PDTVersion;
import com.piece_framework.makegood.aspect.org.eclipse.php.debug.core.aspect.SystemIncludePathAspect;
import com.piece_framework.makegood.aspect.org.eclipse.php.debug.core.aspect.XdebugLaunchAspect;

public class AspectManifest implements com.piece_framework.makegood.aspect.AspectManifest {
    @Override
    public String pluginId() {
        return Fragment.PLUGIN_ID;
    }

    @Override
    public Aspect[] aspects() {
        if (PDTVersion.getInstance().compareTo("3.5.0") >= 0) { //$NON-NLS-1$
            return new Aspect[] {
                new XdebugLaunchAspect(),
            };
        } else {
            return new Aspect[] {
                new XdebugLaunchAspect(),
                new SystemIncludePathAspect(),
            };
        }
    }

    @Override
    public String[] dependencies() {
        if (PDTVersion.getInstance().compareTo("3.5.0") >= 0) { //$NON-NLS-1$
            return new String[] {
                "org.eclipse.core.resources", //$NON-NLS-1$
                "org.eclipse.equinox.common", //$NON-NLS-1$
                "org.eclipse.php.debug.core", //$NON-NLS-1$
                "com.piece_framework.makegood.launch", //$NON-NLS-1$
            };
        } else {
            return new String[] {
                Fragment.PLUGIN_ID,
                "org.eclipse.equinox.common", //$NON-NLS-1$
                "org.eclipse.php.debug.core", //$NON-NLS-1$
                "com.piece_framework.makegood.launch", //$NON-NLS-1$
                "org.eclipse.debug.core", //$NON-NLS-1$
            };
        }
    }
}
