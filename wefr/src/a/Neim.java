package a;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Table;

public class Neim extends ApplicationWindow {
	private Text text;
	private Table table;
	// TODO qualcheco
	// TODO 2
	
	//qualcosa
	
	/**
	 * Create the application window.
	 */
	public Neim() {
		super(null);
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FormLayout());
		
		Composite composite = new Composite(container, SWT.NONE);
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(0, 125);
		fd_composite.right = new FormAttachment(0, 306);
		fd_composite.top = new FormAttachment(0, 35);
		fd_composite.left = new FormAttachment(0, 96);
		composite.setLayoutData(fd_composite);
		
		Spinner spinner = new Spinner(composite, SWT.BORDER);
		spinner.setBounds(10, 10, 54, 27);
		
		SashForm sashForm = new SashForm(container, SWT.NONE);
		FormData fd_sashForm = new FormData();
		fd_sashForm.top = new FormAttachment(0, 82);
		fd_sashForm.left = new FormAttachment(0, 253);
		sashForm.setLayoutData(fd_sashForm);
		
		Menu menu = new Menu(container);
		container.setMenu(menu);
		
		MenuItem mntmSubmenu = new MenuItem(menu, SWT.CASCADE);
		mntmSubmenu.setText("submenu");
		
		Menu menu_1 = new Menu(mntmSubmenu);
		mntmSubmenu.setMenu(menu_1);
		
		MenuItem mntmRadio = new MenuItem(menu, SWT.RADIO);
		mntmRadio.setText("Radio");
		
		Composite composite_1 = new Composite(container, SWT.NONE);
		composite_1.setBounds(10, 96, 64, 64);
		FormData fd_composite_1 = new FormData();
		fd_composite_1.top = new FormAttachment(0, 96);
		fd_composite_1.left = new FormAttachment(0, 10);
		composite_1.setLayoutData(fd_composite_1);
		composite_1.setLayout(new TreeColumnLayout());
		
		Tree tree = new Tree(composite_1, SWT.BORDER);
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		
		CheckboxTreeViewer checkboxTreeViewer = new CheckboxTreeViewer(container, SWT.BORDER);
		Tree tree_1 = checkboxTreeViewer.getTree();
		FormData fd_tree_1 = new FormData();
		fd_tree_1.top = new FormAttachment(0, 10);
		fd_tree_1.left = new FormAttachment(0, 64);
		tree_1.setLayoutData(fd_tree_1);
		
		text = new Text(container, SWT.BORDER);
		FormData fd_text = new FormData();
		fd_text.top = new FormAttachment(composite, 34);
		fd_text.right = new FormAttachment(sashForm, 0, SWT.RIGHT);
		text.setLayoutData(fd_text);
		
		table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		FormData fd_table = new FormData();
		fd_table.bottom = new FormAttachment(composite_1, 33, SWT.BOTTOM);
		fd_table.top = new FormAttachment(0, 43);
		fd_table.right = new FormAttachment(composite, 167, SWT.RIGHT);
		fd_table.left = new FormAttachment(composite, 6);
		table.setLayoutData(fd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		Tree tree_2 = new Tree(container, SWT.BORDER);
		FormData fd_tree_2 = new FormData();
		fd_tree_2.bottom = new FormAttachment(composite_1, -16);
		fd_tree_2.left = new FormAttachment(0, 36);
		tree_2.setLayoutData(fd_tree_2);

		return container;
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Create the menu manager.
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuManager = new MenuManager("menu");
		return menuManager;
	}

	/**
	 * Create the toolbar manager.
	 * @return the toolbar manager
	 */
	@Override
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);
		return toolBarManager;
	}

	/**
	 * Create the status line manager.
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		return statusLineManager;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Neim window = new Neim();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("New Application");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}
}
