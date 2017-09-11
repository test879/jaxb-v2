/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2005-2017 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.xml.txw2.annotation;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.TXW;
import com.sun.xml.txw2.output.XmlSerializer;

import javax.xml.namespace.QName;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.METHOD;

/**
 * Specifies the name of the XML element.
 *
 * <h2>Used on method</h2>
 * <p>
 * When used on methods declared on interfaces that derive
 * from {@link TypedXmlWriter}, it specifies that the invocation
 * of the method will produce an element of the specified name.
 *
 * <p>
 * The method signature has to match one of the following patterns.
 *
 * <dl>
 *  <dt>Child writer: {@code TW foo()}</dt>
 *  <dd>TW must be an interface derived from {@link TypedXmlWriter}.
 *      When this method is called, a new child element is started,
 *      and its content can be written by using the returned {@code TW}
 *      object. This child element will be ended when its _commit method
 *      is called.
 *  <dt>Leaf element: {@code void foo(DT1,DT2,...)}</dt>
 *  <dd>DTi must be datatype objects.
 *      When this method is called, a new child element is started,
 *      followed by the whitespace-separated text data from each of
 *      the datatype objects, followed by the end tag.
 * </dl>
 *
 * <h2>Used on interface</h2>
 * <p>
 * When used on interfaces that derive from {@link TypedXmlWriter},
 * it associates an element name with that interface. This name is
 * used in a few places, such as in {@link TXW#create(Class,XmlSerializer)}
 * and {@link TypedXmlWriter#_element(Class)}.
 *
 *
 * @author Kohsuke Kawaguchi
 */
@Retention(RUNTIME)
@Target({METHOD,TYPE})
public @interface XmlElement {
    /**
     * The local name of the element.
     */
    String value() default "";

    /**
     * The namespace URI of this element.
     *
     * <p>
     * If the annotation is on an interface and this paramter is left unspecified,
     * then the namespace URI is taken from {@link XmlNamespace} annotation on
     * the package that the interface is in. If {@link XmlNamespace} annotation
     * doesn't exist, the namespace URI will be "".
     *
     * <p>
     * If the annotation is on a method and this parameter is left unspecified,
     * then the namespace URI is the same as the namespace URI of the writer interface.
     */
    String ns() default "##default";
}
