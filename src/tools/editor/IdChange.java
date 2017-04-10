package tools.editor;

import javafx.scene.control.TextField;

abstract class IdChange extends TextField {
	
	@Override
	public void replaceText(int start, int end, String text) {
		if(validate(text)) {
			super.replaceText(start, end, text);
		}
	}
	
	@Override
	public void replaceSelection(String text) {
		if(validate(text) && check(Integer.parseInt(text))) {
			super.replaceSelection(text);
		}
	}
	
	boolean validate(String text) {
		return true;
	}
	
	public abstract boolean check(int input);
}
