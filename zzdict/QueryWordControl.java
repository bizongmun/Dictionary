import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class QueryWordControl  {
	/**
	 * parent control
	 */
	Composite parent;
	Group queryWordGroup;
	
	/**
	 * ToolBar that contains 
	 */
	ToolBar toolbar;
	Group toolbarGroup;
	ToolItem back;
	ToolItem forward;
	
	Image backImage, forwardImage, queryImage;
	
	/**
	 * backButton, users click it to view previous queried word
	 */
	Button backButton;
	
	/**
	 * forwordButton, users click it to view next queried word
	 */
	Button forwardButton;
	
	/**
	 * queryButton, users click it to query word's explanation
	 */
	Button queryButton;
	
	/**
	 * input field to input word
	 */
	Text wordInputField;
	
	/**
	 * browser to display word's explanation
	 */
	Browser explanationBrowser;
	
	/**
	 * a toggle button to show autoScan status 
	 */
	Button autoScanStatusCheckBox;
	
	static ResourceBundle resourceBundle;
	
	/**
	 * Default constructor 
	 */
	public QueryWordControl(Composite parent) {
		super();
		this.parent = parent;
	}

	/**
	 * Gets a string from the resource bundle.
	 * We don't want to crash because of a missing String.
	 * Returns the key if not found.
	 */
	static String getResourceString(String key) {
		try {
			return resourceBundle.getString(key);
		} catch (MissingResourceException e) {
			return key;
		} catch (NullPointerException e) {
			return "!" + key + "!";
		}			
	}
	
	void createQueryWordGroups(){
		queryWordGroup = new Group(parent,SWT.NONE);
		queryWordGroup.setLayout(new GridLayout());
		queryWordGroup.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		
		toolbarGroup = new Group(queryWordGroup, SWT.NONE);
		toolbarGroup.setLayout(new FormLayout());
	}
		
	void createToolBar(){
		toolbar = new ToolBar(toolbarGroup, SWT.NONE);
		FormData data = new FormData();
		data.top = new FormAttachment(0, 5);
		toolbar.setLayoutData(data);
		
		back = new ToolItem(toolbar, SWT.PUSH);
		back.setImage(backImage);
		back.setToolTipText("back");
		
		forward = new ToolItem(toolbar, SWT.PUSH);
		forward.setImage(forwardImage);
		forward.setToolTipText("forward");
		
		
	}
	
	/**
	 * main method to test swt code
	 */
	public static void main(String[] args) {
		Display display = new Display ();
		Shell shell = new Shell (display);
		QueryWordControl control = new QueryWordControl(shell);
		control.createQueryWordGroups();
		control.createToolBar();
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}

}
