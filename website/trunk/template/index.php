<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 <?php $templateDir = $GLOBALS['mosConfig_live_site'] ."/templates/" . $GLOBALS['cur_template']; ?>
	<?php mosShowHead(); ?>
	<link href="<?php echo $templateDir; ?>/css/style.php" rel="stylesheet" type="text/css" />
<?php
if ($my->id) {
	initEditor();
}
?>
</head>

<body>
<div id="container">
	<div id="header"><div>
		<h1><a href="#">Flex<strong style="color:#009900">Tools</strong></a></h1>
		<ul id="nav">
			<li class="on"><a href="index.php">home</a></li>

			<li><a href="index.php?option=com_content&task=view&id=25&Itemid=27">about</a></li>
			<li><a href="index.php?option=com_content&task=view&id=26&Itemid=32">download</a></li>
			<li><a href="index.php?option=com_content&task=section&id=3&Itemid=32">FAQ</a></li>
			<li><a href="/index.html">contact</a></li>
		</ul>
	</div></div>
	<div id="strike"><div class="home">
	  <p>Adding the bits that Adobe forgot.</p>
	</div></div>

	<div id="body">
		<div id="l">
			<?php mosMainBody(); ?>
		</div>		
		<div id="r">
			<h2>Find out more </h2>

			<p><span><img src="<?php echo $templateDir; ?>/images/sample-cactus.jpg" alt="sample" width="205" height="28" /><br />
			</span><br />
		    <a href="features.html#launching"><strong>New launch profiles</strong></a> to provide you with a few extra handy features, plus allowing FlexBuilder to work the same way as the rest of Eclipse.</p>
			<hr />
			<p><span><span><img src="<?php echo $templateDir; ?>/images/sample-cactus.jpg" alt="sample" width="205" height="28" /></span></span><br />
		      <br />	
		      <a href="features.html#refactoring"><strong>Refactoring support</strong></a> - don't wait for 
		      Flex Builder 3 to come out to give you some refactoring. Get it now, for Flex Builder 2.
	        </p>	
			<hr />			
			<div class="block">
				<a href="http://www.eclipse.org/">Get Eclipse Platform 3.2</a><br/>
				<a href="http://www.adobe.com/">Get Flex Builder Plug-in</a><br/>
				<a href="http://flextools.berlios.de/">Get Flex Tools</a>
				<p>Get coding.</p>
			</div>		
<?php mosLoadModules ( 'left' ); ?>
		</div>
	</div>
<br clear="all"/></div>	

<div id="footer"><div><div>
	Copyright &copy; 2007 Oliver B. Tupman, All Rights Reserved.
	 |  Designed by:<!-- PLEASE DON'T REMOVE THIS LINK --> <a href="http://www.alltechnologydirectory.com">Technology Directory</a> <!-- THANKS AGAIN! -->
</div>
</div></div>
</body>
</html>