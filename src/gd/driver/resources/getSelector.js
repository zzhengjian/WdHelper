
var utils = {};

utils.getXPathFromNode = function(a) {
	var result = "";
	var stop = false;
	var usingClass = true
	var absolute = false;

	var parent = a.ownerDocument;
	while (a && a != parent && !stop) {
		var str = "";
		var position = utils.getNodePosition(a);

		var name = a.tagName.toLowerCase();
		if(!absolute && a.id && a.id != "") {
			str = ".//*[@id='" + a.id + "']";
			position = null;
			stop = true;
		}else if(!absolute && a.className && a.className != "" && utils.isClassNameUnique(a) && usingClass) {
			str = ".//*[@class='" + a.className + "']";
			position = null;
			stop = true;	
		}else if(!absolute && utils.isTagNameUnique(a)) {
			str = ".//" + name;
			position = null;
			stop = true;	
		} else {
			str = name;
		}
		
		result = str + (position ? "[" + position + "]" : "") + (result? "/": "") + result;		
		a = a.parentElement;
	}

	return result;
};

utils.getCssSelectorFromNode = function(a){
	var result = "";
	var	parent = a.ownerDocument;
	var	stop = false;
	var	str = "";
	var classpath = "";
	var _for = "";
	
	while (a && a != parent && !stop){
		if(a.id)
		{
			if(a.id.indexOf(' ')>0)
			{
				str = "[id='" + a.id + "']";
			}
			else
			{
				str = '#' + a.id;
			}	
			stop = true;
		} 
		else if(a.tagName == "LABEL" && utils.isInForm(a) && (_for = utils.getForAttribute(a)))
		{
			str = "[for='" + _for + "']";
			stop = true;
		}
		else if(classpath = utils.getUniqueClassPath(a))
		{
			str = classpath;
			stop = true;
		} 
		else if(utils.isTagNameUnique(a))
		{
			str = a.tagName.toLowerCase();
			stop = true;
		} 
		else
		{	
			var position = utils.getNodePosition(a);
			if(position)
			{
			   str = a.tagName.toLowerCase() + ':nth-of-type(' + position +  ')';
			}
			else
			{
			   str = a.tagName.toLowerCase()
			}       
		}
		result = str + (result? '>' + result: ''); 
    
		a = a.parentElement;

	}
	return result;
};

utils.getUniqueClassPath = function(a)
{
	var firepathClass = /\s*firepath-matching-node\s*/g;
	var document = a.ownerDocument;
	if(!a.className || !a.className.replace(firepathClass, ''))
	{
		return '';
	}
	
	if(a.className.toLowerCase().indexOf('active') > 0)
	{
		return '';
	}	
	var className = a.className.replace(firepathClass, '').split(' ');
	var len = className.length;
	var selector = '.' + className[len - 1];
	for(i=len - 1; i>=0; i--)
	{		
		if(document.querySelectorAll(selector).length == 1)
			return selector;
		
		selector = '.' + className[i] + selector;
	}

	return '';
};

utils.isInForm = function(a)
{
	if(!a.form)
	{
		return false;
	}
	return true;
};

utils.getForAttribute = function(a)
{
	var toReturn = '';
	var document = a.ownerDocument;
	if(a.hasAttribute('for'))
	{
		toReturn = a.attributes['for'].value;
		if(document.querySelectorAll("[for='" + toReturn + "']").length != 1)
		{
			toReturn = '';
		}		
	}
	
	return toReturn;
};

utils.isClassNameUnique = function(a)
{
	var classname = a.className;
	var doc = a.ownerDocument;
	//avoid to use classname contains too many spaces
	if(classname.split(' ').length > 3) 
	{ 
		return false;
	}		
	return doc.getElementsByClassName(classname).length == 1;
};

utils.isIdUnique = function(a)
{
	var id = a.id;
	var doc = a.ownerDocument;
	
	return doc.querySelectorAll('#'+id).length == 1;
};

utils.isTagNameUnique = function(a)
{
	var tag = a.tagName.toLowerCase();
	var doc = a.ownerDocument;
	return doc.getElementsByTagName(tag).length == 1;
};

utils.getNodePosition = function(a) {
	if (!a.parentNode)
		return null;
	
	var siblings = a.parentNode.childNodes;
	var count = 0;
	var position;
	
	for (var i = 0; i < siblings.length; i++) {
		var object = siblings[i];
		if(object.nodeType == a.nodeType && object.nodeName == a.nodeName) {
			count++;
			if(object == a) position = count;
		}
	}

	if (count > 1)
		return position;
	else
		return null;
};