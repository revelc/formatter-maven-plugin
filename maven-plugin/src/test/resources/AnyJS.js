function APITest(expected, actual, message){

	
	
it('should implement the API: ' + (message||''), function(){ APITest.testKind(expected, actual, message); });




if (!(typeof expected == 'object' || typeof expected == 'function')) { return; }




for (var property in expected) { APITest( expected[property], actual[property], (message ? [message] : []).concat([property]).join('.') ); }




if (typeof expected.prototype == 'object') for (var property in expected.prototype) { APITest( expected.prototype[property], actual.prototype[property], (message ? [message] : []).concat(['prototype',property]).join('.') ); }



}

