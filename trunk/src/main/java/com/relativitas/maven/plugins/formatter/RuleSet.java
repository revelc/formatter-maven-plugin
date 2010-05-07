package com.relativitas.maven.plugins.formatter;

/*
 * Copyright 2010. All work is copyrighted to their respective author(s),
 * unless otherwise stated.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.digester.AbstractObjectCreationFactory;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSetBase;
import org.xml.sax.Attributes;

/**
 * @author jecki
 */
class RuleSet extends RuleSetBase {
	private final Object DUMMY = new Dummy();

	/**
	 * @see org.apache.commons.digester.RuleSetBase#addRuleInstances(org.apache.commons.digester.Digester)
	 */
	public void addRuleInstances(Digester digester) {
		digester.addFactoryCreate("profiles", new ProfilesObjectFactory());
		digester.addFactoryCreate("profiles/profile",
				new ProfileObjectFactory());
		digester.addSetNext("profiles/profile", "add");

		digester.addCallMethod("profiles/profile/setting", "put", 2);
		digester.addCallParam("profiles/profile/setting", 0, "id");
		digester.addCallParam("profiles/profile/setting", 1, "value");
	}

	/**
	 * @author jecki
	 */
	private class ProfilesObjectFactory extends AbstractObjectCreationFactory {

		public Object createObject(Attributes attrs) throws Exception {
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			InvocationHandler handler = new InvocationHandler() {
				private List list = new ArrayList();

				public Object invoke(Object proxy, Method method, Object[] args)
						throws Throwable {

					if ("add".equals(method.getName()) && args.length == 1
							&& DUMMY.equals(args[0])) {
						return Boolean.FALSE;
					}

					return method.invoke(list, args);
				}
			};
			Object proxy = Proxy.newProxyInstance(cl,
					new Class[] { List.class }, handler);
			return proxy;
		}
	}

	/**
	 * @author jecki
	 */
	private class ProfileObjectFactory extends AbstractObjectCreationFactory {

		public Object createObject(Attributes attrs) throws Exception {
			String kind = attrs.getValue("kind");
			if ("CodeFormatterProfile".equals(kind)) {
				return new HashMap();
			}
			return DUMMY;
		}
	}

	/**
	 * @author jecki
	 */
	private class Dummy {
		public void put(Object o1, Object o2) {
			// do nothing
		}
	}
}
