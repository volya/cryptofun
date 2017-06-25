package standaloneui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import encryption.BookCode;

public class EncryptorMain extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6619206999428170526L;
	
	final static private String title = new String("Спутник Криптогафа - CryptoHelper");
//	private String keyFolderPath = "d:/src/cryptoFun/resource";
	private File keyFolder;

    File getKeyFolder() {
		return keyFolder;
	}

	void setKeyFolder(File keyFolder) {
		this.keyFolder = keyFolder;
	}
	private TextArea textArea = new TextArea("", 0,0, TextArea.SCROLLBARS_VERTICAL_ONLY);

//    private Locale storedLocale;
    
    // UI elements
    private MenuItem openFile = new MenuItem();  
    private MenuItem saveFile = new MenuItem(); 
    private MenuItem close = new MenuItem(); 				
	
    private MenuItem bookKey = new MenuItem();
    private MenuItem encrypt = new MenuItem();
    private MenuItem decrypt = new MenuItem();
    private MenuItem keysFolder = new MenuItem();

    private BookCode bookCode; // #TODO diferent alphabeth CyrillicAplhabeth.class);

	public void actionPerformed(ActionEvent e) 
	{
		// file operations
        if (e.getSource() == this.close) {
//        	setLocale(storedLocale);
            this.dispose(); // dispose all resources and close the application
        }
        else if (e.getSource() == this.openFile) {
        	readFromFile();
        }
        else if (e.getSource() == this.saveFile) {
        	saveToFile();
        }

        // encryption operations
        else if (e.getSource() == this.keysFolder) {
        	selectKeyFolder();
        }        
        // book selection for Vegere encryption
        else if (e.getSource() == this.bookKey) {
        	selectPathwordFile();
        }
        
        // encrypt
        else if (e.getSource() == this.encrypt) {        	
        	this.textArea.setText(bookCode.encrypt(this.textArea.getText(),true));
        }
        
        else if (e.getSource() == this.decrypt) {
        	this.textArea.setText(bookCode.encrypt(this.textArea.getText(),false));
        }
	}

	private void selectPathwordFile() {
        JFileChooser open = new JFileChooser(); // open up a file chooser (a dialog for the user to browse files to open)
       	open.setCurrentDirectory(keyFolder);
       	open.setFileSelectionMode(JFileChooser.FILES_ONLY);
       	String [] extensions = {"txt","html"};
       	open.setFileFilter(new FileNameExtensionFilter("Text files", extensions));
    	open.setDialogTitle("Шифровальная книга");
        int option = open.showOpenDialog(this); // get the option that the user selected (approve or cancel)
        // if the user clicked OK, we have "APPROVE_OPTION"
        if (option == JFileChooser.APPROVE_OPTION) {
            try {
//            	File codeBook = open.getSelectedFile();
                bookCode = new BookCode(open.getSelectedFile());
            	encrypt.setEnabled(true);
            	decrypt.setEnabled(true);		
            } catch (Exception ex) { // catch any exceptions, and...
                System.out.println(ex.getMessage());
            }
        }
	}

	private void selectKeyFolder() {
    	JFileChooser  chooser = new JFileChooser(); 
    	chooser.setCurrentDirectory(keyFolder);
    	chooser.setDialogTitle("Books folder");
    	chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);        	
    	//
    	// disable the "All files" option.
    	//
    	chooser.setAcceptAllFileFilterUsed(false);
    	//    
    	if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
    		keyFolder = chooser.getSelectedFile();
    	}
    	else {
    		System.out.println("No Selection ");
    	}
		
	}

	private void saveToFile() {
        JFileChooser save = new JFileChooser(); // again, open a file chooser
        int option = save.showSaveDialog(this); // similar to the open file, only this time we call
        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                // create a buffered writer to write to a file
                BufferedWriter out = new BufferedWriter(new FileWriter(save.getSelectedFile().getPath()));
                out.write(this.textArea.getText()); // write the contents of the TextArea to the file
                out.close(); // close the file stream
            } catch (Exception ex) { // again, catch any exceptions and...
                // ...write to the debug console
                System.out.println(ex.getMessage());
            }
        }		
	}

	private void readFromFile() {
        JFileChooser open = new JFileChooser(); // open up a file chooser (a dialog for the user to browse files to open)
        
        int option = open.showOpenDialog(this); // get the option that the user selected (approve or cancel)
        // NOTE: because we are OPENing a file, we call showOpenDialog~
        // if the user clicked OK, we have "APPROVE_OPTION"
        // so we want to open the file
        if (option == JFileChooser.APPROVE_OPTION) {
            this.textArea.setText(""); // clear the TextArea before applying the file contents
            try {
                // create a scanner to read the file (getSelectedFile().getPath() will get the path to the file)
                Scanner scan = new Scanner(new FileReader(open.getSelectedFile().getPath()));
                while (scan.hasNext()) { // while there's still something to read
                    this.textArea.append(scan.nextLine() + "\n"); // append the line to the TextArea
                }
                scan.close();
                
            } catch (Exception ex) { // catch any exceptions, and...
                // ...write to the debug console
                System.out.println(ex.getMessage());
            }
        }		
	}

	EncryptorMain() 
	{
		super(title);
		keyFolder = new File(System.getProperty("user.home") + "/Desktop");
//		storedLocale = getLocale();
//		setLocale(new Locale("ru","RU","WIN"));
		
		setSize(600,400); // set the initial size of the window		
		setDefaultCloseOperation(EXIT_ON_CLOSE); // set the default close operation (exit when it gets closed)
		textArea.setFont(new Font("Century Gothic", Font.BOLD, 12)); // set a default font for the TextArea

		getContentPane().setLayout(new BorderLayout()); // the BorderLayout bit makes it fill it automatically
		getContentPane().add(textArea);

		// add our menu bar into the GUI
		 
		 MenuBar menuBar = new MenuBar();
		setMenuBar(menuBar);
		
		Menu fileMenu = new Menu(); //  menu
		menuBar.add(fileMenu); // we'll configure this later

		// configure menu items
		fileMenu.setLabel("File");
				
		// "Open" menu option
		openFile.setLabel("Open"); // set the label of the menu item
		openFile.addActionListener(this); // add an action listener (so we know when it's been clicked
		openFile.setShortcut(new MenuShortcut(KeyEvent.VK_O, false)); // set a keyboard shortcut
		fileMenu.add(openFile); // add it to the "File" menu

		// and the save...
		saveFile.setLabel("Save");
		saveFile.addActionListener(this);
		saveFile.setShortcut(new MenuShortcut(KeyEvent.VK_S, false));
		fileMenu.add(saveFile);

		// and finally, the close option
		close.setLabel("Close");

		// along with our "CTRL+F4" shortcut to close the window, we also have
		// the default closer, as stated at the beginning of this tutorial.
		// this means that we actually have TWO shortcuts to close:
		// 1) the default close operation (example, Alt+F4 on Windows)
		// 2) CTRL+F4, which we are about to define now: (this one will appear in the label)
		close.setShortcut(new MenuShortcut(KeyEvent.VK_F4, false));
		close.addActionListener(this);
		fileMenu.add(close);

		// Encryption book menu

		Menu actionMenu = new Menu();
		menuBar.add(actionMenu);
		
		actionMenu.setLabel("Action");
		
		encrypt.setLabel("Encrypt");
		encrypt.setShortcut(new MenuShortcut(KeyEvent.VK_E, false));
		encrypt.setEnabled(false);
		encrypt.addActionListener(this);		
				
		decrypt.setLabel("Decrypt");
		decrypt.setShortcut(new MenuShortcut(KeyEvent.VK_D, false));
		decrypt.setEnabled(false);
		decrypt.addActionListener(this);
		
		bookKey.setLabel("Key book");
		bookKey.setShortcut(new MenuShortcut(KeyEvent.VK_K, false));		
		bookKey.addActionListener(this);
		
		keysFolder.setLabel("Set folder");
		keysFolder.setShortcut(new MenuShortcut(KeyEvent.VK_F, false));		
		keysFolder.addActionListener(this);

		actionMenu.add(keysFolder);
		actionMenu.add(bookKey);
		actionMenu.add(encrypt);
		actionMenu.add(decrypt);		


	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EncryptorMain app = new EncryptorMain();
		if (args != null && args.length > 0 && args[0].isEmpty()) {
			File libPath = new File(args[0]); 
			if (libPath != null) {
				app.setKeyFolder(libPath);
			}
		}
		app.setVisible(true);
	}

}
