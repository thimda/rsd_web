/*
Copyright (c) 2003-2011, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

CKEDITOR.editorConfig = function( config )
{
	config.baseFloatZIndex = 10000000;
	config.width = "100%";
	config.height = "100%";
	
	config.toolbar = 'Custom'; 
	config.toolbar_Full = 
	[ 
	['Source','-','Save','NewPage','Preview','-','Templates'], 
	['Cut','Copy','Paste','PasteText','PasteFromWord','-','Print', 'SpellChecker', 'Scayt'], 
	['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'], 
	['Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'ImageButton', 'HiddenField'], 
	'/', 
	['Bold','Italic','Underline','Strike','-','Subscript','Superscript'], 
	['NumberedList','BulletedList','-','Outdent','Indent','Blockquote'], 
	['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'], 
	['Link','Unlink','Anchor'], 
	['Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak'], 
	'/', 
	['Styles','Format','Font','FontSize'], 
	['TextColor','BGColor'], 
	['Maximize', 'ShowBlocks','-','About'] 
	]; 
	
	
	config.toolbar_Basic = 
	[ 
	['Bold', 'Italic', '-', 'NumberedList', 'BulletedList', '-', 'Link', 'Unlink','-','About'] 
	];
	
	config.toolbar_Custom = 
	[ 
	['Bold', 'Italic', '-', 'NumberedList', 'BulletedList', '-', 'Link', 'Unlink','-','Image','Flash'] 
	];
};
