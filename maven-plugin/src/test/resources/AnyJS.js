/*
 * Copyright 2010-2016. All work is copyrighted to their respective
 * author(s), unless otherwise stated.
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
function APITest(expected, actual, message){

	
	
it('should implement the API: ' + (message||''), function(){ APITest.testKind(expected, actual, message); });




if (!(typeof expected == 'object' || typeof expected == 'function')) { return; }




for (var property in expected) { APITest( expected[property], actual[property], (message ? [message] : []).concat([property]).join('.') ); }




if (typeof expected.prototype == 'object') for (var property in expected.prototype) { APITest( expected.prototype[property], actual.prototype[property], (message ? [message] : []).concat(['prototype',property]).join('.') ); }



}

