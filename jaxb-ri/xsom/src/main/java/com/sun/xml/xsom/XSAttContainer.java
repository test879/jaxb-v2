/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://oss.oracle.com/licenses/CDDL+GPL-1.1
 * or LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.xml.xsom;

import java.util.Iterator;
import java.util.Collection;

/**
 * Common aspect of {@link XSComplexType} and {@link XSAttGroupDecl}
 * as the container of attribute uses/attribute groups.
 * 
 * @author
 *     Kohsuke Kawaguchi (kohsuke.kawaguchi@sun.com)
 */
public interface XSAttContainer extends XSDeclaration {
    XSWildcard getAttributeWildcard();
    
    /**
     * Looks for the attribute use with the specified name from
     * all the attribute uses that are directly/indirectly
     * referenced from this component.
     * 
     * <p>
     * This is the exact implementation of the "attribute use"
     * schema component.
     */
    XSAttributeUse getAttributeUse( String nsURI, String localName );
    
    /**
     * Lists all the attribute uses that are directly/indirectly
     * referenced from this component.
     * 
     * <p>
     * This is the exact implementation of the "attribute use"
     * schema component.
     */
    Iterator<? extends XSAttributeUse> iterateAttributeUses();

    /**
     * Gets all the attribute uses.
     */
    Collection<? extends XSAttributeUse> getAttributeUses();

    /**
     * Looks for the attribute use with the specified name from
     * the attribute uses which are declared in this complex type.
     * 
     * This does not include att uses declared in att groups that
     * are referenced from this complex type, nor does include
     * att uses declared in base types.
     */
    XSAttributeUse getDeclaredAttributeUse( String nsURI, String localName );
    
    /**
     * Lists all the attribute uses that are declared in this complex type.
     */
    Iterator<? extends XSAttributeUse> iterateDeclaredAttributeUses();

    /**
     * Lists all the attribute uses that are declared in this complex type.
     */
    Collection<? extends XSAttributeUse> getDeclaredAttributeUses();


    /**
     * Iterates all AttGroups which are directly referenced from
     * this component.
     */
    Iterator<? extends XSAttGroupDecl> iterateAttGroups();

    /**
     * Iterates all AttGroups which are directly referenced from
     * this component.
     */
    Collection<? extends XSAttGroupDecl> getAttGroups();
}
