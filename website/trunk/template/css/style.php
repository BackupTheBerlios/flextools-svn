<?php header("Content-type: text/css"); ?>
<?php $templateDir = "http://flextools.berlios.de/templates/flex_tools_project_template" ?>
body {
	font-family: "Trebuchet MS", Tahoma, Verdana, sans-serif;
	font-size: 76%; 
	margin: 0; 
	padding: 0;
	color: #666;
	text-align: center;
	}
a { text-decoration: none; border-bottom: 1px solid #ccc; color: #f90; }
a:hover { border: none; }	
h2 {
	color: #333;
	font-size: 16px;
	font-weight: bold;
	clear: both;
	background-image: url(<?php echo $templateDir; ?>/images/dots.gif);
	background-repeat: repeat-x;
	background-position: 0 25px;
	padding-bottom: 10px;
	margin-bottom: -6px;
	}
.contentheading {
	font-family: Century Gothic, Tahoma, Verdana, sans-serif;
	float: left;	
	font-size: 1.9em;
	padding: 30px 0;
	margin: 0;
}
.small {
	font-size: 70%;
	text-align: right;
}
.createdate {
	font-size: 70%;
	text-align: right;
}
#container {
	height: auto ;
	height: 100%;
	min-height: 100%;
	position: relative;
	}
	

#header { border-top: 5px solid #000; }	
#header div {
	margin: 0 auto;
	width: 750px;
	text-align: left;
	padding-left: 20px;
	}
#header h1 { 
	font-family: Century Gothic, Tahoma, Verdana, sans-serif;
	float: left;	
	font-size: 1.9em;
	padding: 30px 0;
	margin: 0;
	}			
#header h1 span { color: #999; }	
#header h1 a {
	border: none;
	color: #000;	
	font-weight: normal;	
	}		

#nav {
	float: right;
	margin: 0;
	padding: 0;
	}
#nav li {
	list-style-type: none;
	display: inline;		
	float: left;	
	}	
#nav li a {
	text-decoration: none;
	border: none;
	color: #000;
	padding-top: 5px;		
	display: block;	
	margin: 0 15px;		
	}		
#nav li.on a { 
	background: url(<?php echo $templateDir; ?>/images/nav_hover.gif) no-repeat 50%;
	background-position: 0px -2px;
	padding: 1px 45px 6px 45px;
	margin: 0;
	color: #fff;
	}
#nav li a:hover { color: #33CC00; }	

#strike {
	background-image:url(<?php echo $templateDir; ?>/images/site-strike.jpg);
	text-align: center;
	clear: both;
	margin-bottom: 35px;
	padding-left: 10px;
	}
#strike div {
	width: 420px;
	margin: 0 auto;
	text-align: left;
	padding: 40px 30px 40px 0;
	}	

#strike p {
	color: #fff;
	margin: 0;
	padding: 0;
	font-size: 20px;
	letter-spacing: -1px;
	}	
	

#body { 
	width: 750px;
	margin: 0 auto;
	text-align: left;
	background: url(<?php echo $templateDir; ?>/images/line_bg.gif) repeat-y 515px 20px;
	padding-left: 20px;
	padding-bottom: 61px;	
	}
#body:after { 
	content: "."; 
	display: block; 
	clear: both; 
	visibility: hidden; 
	height: 0;
	}
* html #body { height: 1%; }

#l, 
#l {
	width: 480px;
	padding-right: 20px;
	float: left;
	margin-right: 20px;
	}
#l p,
#r p {
	margin-top: 10px;
	}	

#r {
	width: 210px;
	float: right;
	margin-right:15px;
	}
#r p span {
	color: #009900;
	font-weight: bold;
	}
#r p {
	margin: 1em 0;
	padding: 0;
}	
#r hr {
	border-top: 1px solid #eee; 
	height: 1px; 
	border-left: 0; 
	border-right: 0; 
	border-bottom: 0; 
	margin: 0; 	
	}			

div.block {
	background-color: #F8F8F8;
	border: 1px solid #ddd;
	padding: 20px;
	text-align: center;
	color: #999;
	margin-top: 10px;
	}	
	
#footer {
	margin-top: -61px;
	color: #fff;
	padding-top: 30px;
	width: 100%;
	z-index: 500;
	position: relative;	
	border-bottom: 7px solid #000;	
	background: #fff;
	}
#footer div {
	width: 800px;
	background-color:#000000;
	margin: 0 auto;
	}	
#footer div div {	
	padding-top: 10px;	
	padding-bottom: 2px;
	font-size: 10px;
	}
#footer a { color: #fff; font-weight: bold; }		
