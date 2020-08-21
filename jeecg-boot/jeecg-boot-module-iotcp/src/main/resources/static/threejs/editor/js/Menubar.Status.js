/**
 * @author mrdoob / http://mrdoob.com/
 */

import * as THREE from '../../build/three.module.js';

import { UIPanel, UIButton, UIText } from './libs/ui.js';
import { UIBoolean } from './libs/ui.three.js';

var MenubarStatus = function ( editor ) {

	var strings = editor.strings;

	var container = new UIPanel();
	container.setClass( 'menu right' );

	var autosave = new UIBoolean( editor.config.getKey( 'autosave' ), strings.getKey( 'menubar/status/autosave' ) );
	autosave.text.setColor( '#888' );
	autosave.onChange( function () {

		var value = this.getValue();

		editor.config.setKey( 'autosave', value );

		if ( value === true ) {

			editor.signals.sceneGraphChanged.dispatch();

		}

	} );
	container.add( autosave );

	editor.signals.savingStarted.add( function () {

		autosave.text.setTextDecoration( 'underline' );

	} );

	editor.signals.savingFinished.add( function () {

		autosave.text.setTextDecoration( 'none' );

	} );

	var saveButton = new UIButton(strings.getKey( 'menubar/status/saveButton' ) );

	saveButton.onClick( function () {
		editor.config.setKey('autosave', true);
		editor.signals.sceneGraphChanged.dispatch();

	} );
	container.add( saveButton );

	editor.signals.savingStarted.add( function () {
		saveButton.setDisabled(true);
	} );

	editor.signals.savingFinished.add( function () {
		saveButton.setDisabled(false);

	} );

	var version = new UIText( 'r' + THREE.REVISION );
	version.setClass( 'title' );
	version.setOpacity( 0.5 );
	container.add( version );

	return container;

};

export { MenubarStatus };
