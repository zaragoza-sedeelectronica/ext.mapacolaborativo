/*
 * Copyright (c) 2014, IAAA
 * Componente que muestra la información "semántica" correspondiente a una calle del callejero de Zaragoza
 * @author rodolfo
 * @author rociorm (integracion del modulo de extraccion semantica de rodolfo en el callejero)
 */
var mappings=new Object();var wikiData=new Object();var clientID=SEMANTIC_MAPPINGS_SERVICE_CLIENT_ID;var secretKey=SEMANTIC_MAPPINGS_SERVICE_KEY;var clientProtocol;try{clientProtocol=window.location.protocol+""}catch(e){clientProtocol="http:"}function loadSemanticMappings(streetId,callback){datauri=clientProtocol+SEMANTIC_MAPPINGS_SERVICE_URL+".json?q=linkedResource.id=="+streetId+"&sort=status,score";function doRequest(url,callbackfunction){var saveServletReq;var result=null;function parseResponse(){if(this.readyState==4){result=this.responseText;data=eval("("+result+")");if(typeof callbackfunction=="function"){callbackfunction(streetId,data,callback)}}}if(window.XMLHttpRequest){saveServletReq=new XMLHttpRequest();saveServletReq.onreadystatechange=parseResponse;saveServletReq.open("Get",url,true);saveServletReq.send(null)}else{if(window.ActiveXObject){saveServletReq=new ActiveXObject("Microsoft.XMLHTTP");if(saveServletReq){saveServletReq.onreadystatechange=parseResponse;saveServletReq.url=url;saveServletReq.send()}}}}doRequest(datauri,loadSemanticMappingsCallback)}function loadSemanticMappingsCallback(b,c,d){var a=false;if(c&&(c.totalCount>0)&&(typeof(c.result)!="undefined")){first=true;mappings[b]=new Array();$.each(c.result.reverse(),function(g,i){mappings[b][g]=i;var h=(i.matchedResourceUri.indexOf("es.dbpedia.org")>-1);var f;if(h){dbPediaUri=i.matchedResourceUri;f=dbPediaUri.split("/").pop()}if(first){first=false;if(h){a=true;getWikipediaContent(f,b,d)}}})}if(!a){d(b)}}function getWikipediaContentFromAPIResponse(b){var a="";var c=$("<div>"+b.text["*"]+"</div>");var d=$("p",c).first().html();d=d.replace(/href="\//g,'href="http://es.wikipedia.org/');var f=$(".image img",c);f=typeof f.first()[0]==="object"?f="http:"+f.first()[0].getAttribute("src"):"";a=a+'<div class="mainDescription">';a=a+d;if(f!=""){a=a+'<img src="'+f+'" class="mainImage">'}a=a+"</div>";return a}function getWikipediaContentFromAPIResponse2(c,b){var a="";var i=false,f=0,d=$(c.parse.text["*"]),g;while(i===false){i=true;g=d.filter("p:eq("+f+")").html();if((typeof(g)!="undefined")&&(g.indexOf("<span")===0)){f++;i=false}}var h=$(".image img",d);h=typeof h.first()[0]==="object"?h="http:"+h.first()[0].getAttribute("src"):"";a=a+'<div class="mainDescription">';g=jQuery.truncate(g,{length:250,words:true});a=a+g;if(h!=""){h=h.replace(new RegExp("http:http://","g"),"http://");h=h.replace(new RegExp("http:https://","g"),"https://");a=a+'<img src="'+h+'" class="mainImage">'}a=a+"</div>";a=a.replace(new RegExp('href="/wiki/',"g"),'target="_blank" href="http://es.wikipedia.org/wiki/');a=a.replace(new RegExp('href="'+window.location.protocol+"//"+window.location.host+"/wiki/","g"),'target="_blank" href="http://es.wikipedia.org/wiki/');a=a.replace(new RegExp('href="#',"g"),'target="_blank" href="http://es.wikipedia.org/wiki/'+b+"#");a=a.replace(new RegExp('href="'+window.location.href+"#","g"),'target="_blank" href="http://es.wikipedia.org/wiki/'+b+"#");return a}function getImageUrl(a){$.ajax({type:"GET",url:clientProtocol+"//es.wikipedia.org/w/api.php?action=query&titles="+a+"&prop=pageimages&format=json&pithumbsize=100&callback=?",encoding:"UTF-8",dataType:"json",async:false}).done(function(b){for(key in b.query.pages){if(typeof b.query.pages[key].thumbnail!="undefined"){return b.query.pages[key].thumbnail.source}}return""}).fail(function(b,c){if(typeof console!="undefined"){console.log("Request failed: "+c)}return""})}function getWikipediaContent(b,a,d){var c;$.ajax({type:"GET",url:clientProtocol+"//es.wikipedia.org/w/api.php?format=json&action=parse&page="+b+"&callback=?",encoding:"UTF-8",async:false,dataType:"json"}).success(function(g){c=g;var f="";if(typeof(c)!="undefined"){f=getWikipediaContentFromAPIResponse2(c,b);wikiData[b]=f;d(a)}})}function getSemanticContent(b){var c="";if(typeof(mappings[b])!="undefined"){var f=true;var a=mappings[b].length;$.each(mappings[b],function(h,j){var i=(j.matchedResourceUri.indexOf("es.dbpedia.org")>-1);var g;if(i){dbPediaUri=j.matchedResourceUri;g=dbPediaUri.split("/").pop()}if(f){f=false;if(i){if(typeof wikiData[g]!="undefined"){c=c+'<a href="http://es.wikipedia.org/wiki/'+g+'" class="wikipediaLink" target="_blank">'+j.matchedResourceName+"</a>";c=c+wikiData[g]}else{c=c+'<a href="http://es.wikipedia.org/wiki/'+g+'" class="externalLink" target="_blank">'+j.matchedResourceName+"</a><br>"}if(a>1){c=c+'<h4 class="otherLinks">'+messages["search-aditional-info-other-links"]+"</h4>"}}else{c=c+'<a href="'+j.matchedResourceUri+'" class="externalLink" target="_blank">'+j.matchedResourceName+"</a><br>"}}else{if(i){c=c+'<a href="http://es.wikipedia.org/wiki/'+g+'" class="wikipediaLink" target="_blank">'+j.matchedResourceName+"</a><br>"}else{c=c+'<a href="'+j.matchedResourceUri+'" class="externalLink" target="_blank">'+j.matchedResourceName+"</a><br>"}}});var d="<style>.resultDetailMapTipDescription {margin-top: 10px;} .resultDetailMapTipDescription a {color: #000000;} .resultDetailMapTipDescription img {max-width: 150px;	max-height: 150px;} .resultDetailMapTipDescription .mainImage {display: block; margin-top: 10px; margin-bottom: 10px;  margin-left: auto;  margin-right: auto;	*height: 150px;} .resultDetailMapTipDescription .mainDescription {margin-top: 10px;} .otherLinks { font-size: 12px; font-weight: bold; margin-bottom: 5px; margin-top: 5px;} .wikipediaLink { background : url("+DEPLOYMENT_LOCATION+"css/callejero/images/wikipedia.png) no-repeat bottom left; padding-left: 20px; } .externalLink { background : url("+DEPLOYMENT_LOCATION+"css/callejero/images/link.png) no-repeat bottom left; padding-left: 20px; } </style>";c="<html><head>"+d+'</head><body><div class="resultDetailMapTipDescription">'+c+"</div></body></html>"}else{}return c};