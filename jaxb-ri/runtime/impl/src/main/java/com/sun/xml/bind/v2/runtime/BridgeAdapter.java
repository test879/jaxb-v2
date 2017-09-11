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

package com.sun.xml.bind.v2.runtime;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;

import com.sun.istack.NotNull;
import com.sun.xml.bind.api.Bridge;
import com.sun.xml.bind.api.TypeReference;
import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallerImpl;

import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * {@link Bridge} decorator for {@link XmlAdapter}.
 *
 * @author Kohsuke Kawaguchi
 */
final class BridgeAdapter<OnWire,InMemory> extends InternalBridge<InMemory> {
    private final InternalBridge<OnWire> core;
    private final Class<? extends XmlAdapter<OnWire,InMemory>> adapter;

    public BridgeAdapter(InternalBridge<OnWire> core, Class<? extends XmlAdapter<OnWire,InMemory>> adapter) {
        super(core.getContext());
        this.core = core;
        this.adapter = adapter;
    }

    public void marshal(Marshaller m, InMemory inMemory, XMLStreamWriter output) throws JAXBException {
        core.marshal(m,adaptM(m,inMemory),output);
    }

    public void marshal(Marshaller m, InMemory inMemory, OutputStream output, NamespaceContext nsc) throws JAXBException {
        core.marshal(m,adaptM(m,inMemory),output,nsc);
    }

    public void marshal(Marshaller m, InMemory inMemory, Node output) throws JAXBException {
        core.marshal(m,adaptM(m,inMemory),output);
    }

    public void marshal(Marshaller context, InMemory inMemory, ContentHandler contentHandler) throws JAXBException {
        core.marshal(context,adaptM(context,inMemory),contentHandler);
    }

    public void marshal(Marshaller context, InMemory inMemory, Result result) throws JAXBException {
        core.marshal(context,adaptM(context,inMemory),result);
    }

    private OnWire adaptM(Marshaller m,InMemory v) throws JAXBException {
        XMLSerializer serializer = ((MarshallerImpl)m).serializer;
        serializer.pushCoordinator();
        try {
            return _adaptM(serializer, v);
        } finally {
            serializer.popCoordinator();
        }
    }

    private OnWire _adaptM(XMLSerializer serializer, InMemory v) throws MarshalException {
        XmlAdapter<OnWire,InMemory> a = serializer.getAdapter(adapter);
        try {
            return a.marshal(v);
        } catch (Exception e) {
            serializer.handleError(e,v,null);
            throw new MarshalException(e);
        }
    }


    public @NotNull InMemory unmarshal(Unmarshaller u, XMLStreamReader in) throws JAXBException {
        return adaptU(u, core.unmarshal(u,in));
    }

    public @NotNull InMemory unmarshal(Unmarshaller u, Source in) throws JAXBException {
        return adaptU(u, core.unmarshal(u,in));
    }

    public @NotNull InMemory unmarshal(Unmarshaller u, InputStream in) throws JAXBException {
        return adaptU(u, core.unmarshal(u,in));
    }

    public @NotNull InMemory unmarshal(Unmarshaller u, Node n) throws JAXBException {
        return adaptU(u, core.unmarshal(u,n));
    }

    public TypeReference getTypeReference() {
        return core.getTypeReference();
    }

    private @NotNull InMemory adaptU(Unmarshaller _u, OnWire v) throws JAXBException {
        UnmarshallerImpl u = (UnmarshallerImpl) _u;
        XmlAdapter<OnWire,InMemory> a = u.coordinator.getAdapter(adapter);
        u.coordinator.pushCoordinator();
        try {
            return a.unmarshal(v);
        } catch (Exception e) {
            throw new UnmarshalException(e);
        } finally {
            u.coordinator.popCoordinator();
        }
    }

    void marshal(InMemory o, XMLSerializer out) throws IOException, SAXException, XMLStreamException {
        try {
            core.marshal(_adaptM( XMLSerializer.getInstance(), o ), out );
        } catch (MarshalException e) {
            // recover from error by not marshalling this element.
        }
    }
}
