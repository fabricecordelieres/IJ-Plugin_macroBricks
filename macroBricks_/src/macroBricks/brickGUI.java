package macroBricks;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import macroBricks.bricks.brick;
import macroBricks.bricks.bricksIO;

import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import ij.IJ;
import ij.Prefs;
import ij.plugin.frame.Recorder;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JSplitPane;
import java.awt.event.KeyEvent;

/**
*
*  brickGUI.java, 23 nov. 2017
   Fabrice P Cordelieres, fabrice.cordelieres at gmail.com

   Copyright (C) 2017 Fabrice P. Cordelieres

   License:
   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.
*
*/

/**
 * This class provides a GUI for the macroBrick plugin/stand-alone program, and handles user's actions
 * @author fab
 *
 */
public class brickGUI extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextArea nameTextArea;
	private JButton btnClose;
	private JButton btnAdd;
	private JTextArea functionTextArea;
	private JScrollPane scrollPaneFunction;
	private JLabel lblFunction;
	private JTextArea documentationTextArea;
	private JScrollPane scrollPaneDocumentation;
	private JLabel lblDescription;
	private JScrollPane scrollPaneName;
	private JLabel lblName;
	private JComboBox<String> list;
	private JLabel lblList;
	private JButton btnUpdate;
	private JButton btnDelete;
	private JButton btnPush;
	
	/** Keep track of the bricks **/
	private ArrayList<brick> bricks=null;
	
	/** Keep track of the push button display status **/
	private boolean showPush=false;
	private JSplitPane panelDocFunc;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					brickGUI frame = new brickGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public brickGUI() {
		setTitle("Macro blocks v1.0 - fabrice.cordelieres@gmail.com");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 440, 630);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
		
		lblList = new JLabel("Functions");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblList, 0, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblList, 0, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblList, 24, SpringLayout.NORTH, contentPane);
		contentPane.add(lblList);
		
		list = new JComboBox<String>();
		sl_contentPane.putConstraint(SpringLayout.NORTH, list, 0, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, list, 24, SpringLayout.NORTH, contentPane);
		list.setMaximumRowCount(15);
		list.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateDisplay();
			}
		});
		list.setFocusCycleRoot(true);
		list.setToolTipText("<html>Use the drop-down list to select a function<br>\nto explore or to push to the macro recorder.</html>");
		sl_contentPane.putConstraint(SpringLayout.WEST, list, 6, SpringLayout.EAST, lblList);
		sl_contentPane.putConstraint(SpringLayout.EAST, list, 0, SpringLayout.EAST, contentPane);
		contentPane.add(list);
		
		lblName = new JLabel("Name");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblName, 4, SpringLayout.SOUTH, lblList);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblName, 0, SpringLayout.WEST, contentPane);
		contentPane.add(lblName);
		
		scrollPaneName = new JScrollPane();
		sl_contentPane.putConstraint(SpringLayout.NORTH, scrollPaneName, 4, SpringLayout.SOUTH, lblName);
		sl_contentPane.putConstraint(SpringLayout.WEST, scrollPaneName, 0, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, scrollPaneName, 0, SpringLayout.EAST, contentPane);
		contentPane.add(scrollPaneName);
		
		nameTextArea = new JTextArea();
		nameTextArea.setWrapStyleWord(true);
		nameTextArea.setLineWrap(true);
		nameTextArea.setEditable(false);
		nameTextArea.setToolTipText("<html>Nickname given to the function.<br>\nIt is used to sort the list, and is not used<br>\nwithin the macros.</html>");
		sl_contentPane.putConstraint(SpringLayout.NORTH, nameTextArea, 6, SpringLayout.SOUTH, lblName);
		sl_contentPane.putConstraint(SpringLayout.WEST, nameTextArea, 0, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, nameTextArea, 25, SpringLayout.SOUTH, lblName);
		sl_contentPane.putConstraint(SpringLayout.EAST, nameTextArea, 0, SpringLayout.EAST, contentPane);
		scrollPaneName.setViewportView(nameTextArea);
		
		btnAdd = new JButton("Add");
		btnAdd.setToolTipText("<html>Once the informations have been entered,<br>\npress Ok to send the new function and its<br>\ninformations to the macroBlock.txt file, saved<br>\nwithin the same folder as the plugin.<br><br>\n<b>Shortcuts</b>: Control/Option+N to add, Control/Option+S to save, Esc to cancel</html>");
		sl_contentPane.putConstraint(SpringLayout.WEST, btnAdd, 6, SpringLayout.WEST, contentPane);
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addAction();
			}
		});
		sl_contentPane.putConstraint(SpringLayout.SOUTH, btnAdd, 0, SpringLayout.SOUTH, contentPane);
		contentPane.add(btnAdd);
		
		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateAction();
			}
		});
		btnUpdate.setToolTipText("<html>Once you’ve modified the informations<br>\nof the currently selected function, press update to send<br>\nthe updated function to the macroBlock.txt file, saved<br>\nwithin the same folder as the plugin.<br><br>\n<b>Shortcuts</b>: Control/Option+U to update, Control/Option+S to save, Esc to cancel</html>");
		sl_contentPane.putConstraint(SpringLayout.WEST, btnUpdate, 6, SpringLayout.EAST, btnAdd);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, btnUpdate, 0, SpringLayout.SOUTH, btnAdd);
		contentPane.add(btnUpdate);
		
		btnDelete = new JButton("Delete");
		sl_contentPane.putConstraint(SpringLayout.SOUTH, btnDelete, 0, SpringLayout.SOUTH, contentPane);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteAction();
			}
		});
		btnDelete.setToolTipText("<html>Deletes the current function from the<br>\nmacroBlock.txt file, saved within the same folder<br>\nas the plugin.<br><br>\n<b>Shortcuts</b>: Control/Option+N to delete, Control/Option+S to save, Esc to cancel</html>");
		sl_contentPane.putConstraint(SpringLayout.WEST, btnDelete, 6, SpringLayout.EAST, btnUpdate);
		contentPane.add(btnDelete);
		
		btnPush = new JButton("Push");
		sl_contentPane.putConstraint(SpringLayout.SOUTH, btnPush, 0, SpringLayout.SOUTH, contentPane);
		btnPush.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pushAction();
			}
		});
		btnPush.setToolTipText("<html>Sends the current function to<br>\nthe macro recorder if opened, or to a new<br>\nrecorder in none is yet available.<br><br>\n<b>Shortcuts</b>: Control/Option+P to push</html>");
		sl_contentPane.putConstraint(SpringLayout.WEST, btnPush, 6, SpringLayout.EAST, btnDelete);
		contentPane.add(btnPush);
		
		btnClose = new JButton("Close");
		btnClose.setToolTipText("<html>Quits the plugin</html>");
		sl_contentPane.putConstraint(SpringLayout.WEST, btnClose, 6, SpringLayout.EAST, btnPush);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, btnClose, 0, SpringLayout.SOUTH, contentPane);
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAction();
			}
		});
		contentPane.add(btnClose);
		
		panelDocFunc = new JSplitPane();
		sl_contentPane.putConstraint(SpringLayout.NORTH, panelDocFunc, 4, SpringLayout.SOUTH, scrollPaneName);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, panelDocFunc, -4, SpringLayout.NORTH, btnAdd);
		panelDocFunc.setContinuousLayout(true);
		panelDocFunc.setBorder(null);
		panelDocFunc.setResizeWeight(0.65);
		panelDocFunc.setOrientation(JSplitPane.VERTICAL_SPLIT);
		sl_contentPane.putConstraint(SpringLayout.WEST, panelDocFunc, 0, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, panelDocFunc, 0, SpringLayout.EAST, contentPane);
		contentPane.add(panelDocFunc);
		
		JPanel panel_documentation = new JPanel();
		panelDocFunc.setLeftComponent(panel_documentation);
		SpringLayout sl_panel_documentation = new SpringLayout();
		panel_documentation.setLayout(sl_panel_documentation);
		
		lblDescription = new JLabel("Documentation");
		sl_panel_documentation.putConstraint(SpringLayout.NORTH, lblDescription, 0, SpringLayout.NORTH, panel_documentation);
		sl_panel_documentation.putConstraint(SpringLayout.WEST, lblDescription, 0, SpringLayout.WEST, panel_documentation);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblDescription, 76, SpringLayout.WEST, panel_documentation);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblDescription, -61, SpringLayout.SOUTH, panel_documentation);
		panel_documentation.add(lblDescription);
		
		scrollPaneDocumentation = new JScrollPane();
		sl_panel_documentation.putConstraint(SpringLayout.NORTH, scrollPaneDocumentation, 4, SpringLayout.SOUTH, lblDescription);
		sl_panel_documentation.putConstraint(SpringLayout.WEST, scrollPaneDocumentation, 0, SpringLayout.WEST, panel_documentation);
		sl_panel_documentation.putConstraint(SpringLayout.SOUTH, scrollPaneDocumentation, 0, SpringLayout.SOUTH, panel_documentation);
		sl_panel_documentation.putConstraint(SpringLayout.EAST, scrollPaneDocumentation, 0, SpringLayout.EAST, panel_documentation);
		sl_contentPane.putConstraint(SpringLayout.WEST, scrollPaneDocumentation, 52, SpringLayout.WEST, panel_documentation);
		sl_contentPane.putConstraint(SpringLayout.EAST, scrollPaneDocumentation, -203, SpringLayout.EAST, contentPane);
		panel_documentation.add(scrollPaneDocumentation);
		sl_contentPane.putConstraint(SpringLayout.NORTH, scrollPaneDocumentation, 28, SpringLayout.SOUTH, scrollPaneName);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, scrollPaneDocumentation, -239, SpringLayout.SOUTH, contentPane);
		
		documentationTextArea = new JTextArea();
		documentationTextArea.setWrapStyleWord(true);
		documentationTextArea.setLineWrap(true);
		documentationTextArea.setEditable(false);
		documentationTextArea.setToolTipText("<html>Documentation associated to the function.<br>\nThe macro repository is written on the disk using markdown:<br>\nyou may use its tags to get a formatted documentation from<br>\nthe file (macroBlocks.md), located either in the same folder as <br>\nthe stand alone version et the root of your ImageJ/FiJi installation<br> \nIt is not used when pushing the code to the macro recorder.</html>");
		sl_contentPane.putConstraint(SpringLayout.WEST, documentationTextArea, 0, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, documentationTextArea, 200, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, documentationTextArea, 0, SpringLayout.EAST, contentPane);
		scrollPaneDocumentation.setViewportView(documentationTextArea);
		
		JPanel panel_function = new JPanel();
		panelDocFunc.setRightComponent(panel_function);
		SpringLayout sl_panel_function = new SpringLayout();
		panel_function.setLayout(sl_panel_function);
		
		lblFunction = new JLabel("Function");
		sl_panel_function.putConstraint(SpringLayout.NORTH, lblFunction, 0, SpringLayout.NORTH, panel_function);
		sl_panel_function.putConstraint(SpringLayout.WEST, lblFunction, 0, SpringLayout.WEST, panel_function);
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblFunction, 69, SpringLayout.NORTH, panel_function);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblFunction, 70, SpringLayout.WEST, panel_function);
		panel_function.add(lblFunction);
		
		scrollPaneFunction = new JScrollPane();
		sl_panel_function.putConstraint(SpringLayout.NORTH, scrollPaneFunction, 4, SpringLayout.SOUTH, lblFunction);
		sl_panel_function.putConstraint(SpringLayout.WEST, scrollPaneFunction, 0, SpringLayout.WEST, panel_function);
		sl_panel_function.putConstraint(SpringLayout.SOUTH, scrollPaneFunction, 0, SpringLayout.SOUTH, panel_function);
		sl_panel_function.putConstraint(SpringLayout.EAST, scrollPaneFunction, 0, SpringLayout.EAST, panel_function);
		panel_function.add(scrollPaneFunction);
		
		functionTextArea = new JTextArea();
		functionTextArea.setWrapStyleWord(true);
		functionTextArea.setLineWrap(true);
		functionTextArea.setEditable(false);
		functionTextArea.setToolTipText("<html>Core macro code for the function.<br>\nThis text will be send to the macro recorder<br>\nwhen the push button is pressed.</html>");
		sl_contentPane.putConstraint(SpringLayout.WEST, functionTextArea, 0, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, functionTextArea, -6, SpringLayout.NORTH, btnAdd);
		sl_contentPane.putConstraint(SpringLayout.EAST, functionTextArea, 0, SpringLayout.EAST, contentPane);
		scrollPaneFunction.setViewportView(functionTextArea);
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				if(e.getID()==KeyEvent.KEY_PRESSED) {
					if(e.isControlDown() ||e.isMetaDown()) {
						switch(e.getKeyCode()) {
							case KeyEvent.VK_N: addAction(); break;
							case KeyEvent.VK_U: updateAction(); break;
							case KeyEvent.VK_D: deleteAction(); break;
							case KeyEvent.VK_S: 
												if(btnAdd.getText()=="Save") {addAction(); break;}
												if(btnUpdate.getText()=="Save") {updateAction(); break;}
												if(btnDelete.getText()=="Save") {deleteAction();addAction(); break;}
												break;
							case KeyEvent.VK_P: pushAction(); break; 											
						}
					}
					
					if(e.getKeyCode()==KeyEvent.VK_ESCAPE) {
						if(btnClose.getText()=="Cancel") closeAction();
					}
				}
				return false;
			}
			
		});
		
		updateBricksList();
		updateDisplay();
	}
	
	/**
	 * Returns the state of the Push button (visible or not)
	 * @return the state of the Push button, as a boolean
	 */
	public boolean isShowPush() {
		return showPush;
	}

	/**
	 * Replaces the state of the Push button by the provided boolean, then adapts the display accordingly
	 * @param showPush the state of the Push button, as a boolean
	 */
	public void setShowPush(boolean showPush) {
		this.showPush = showPush;
		updateDisplay();
	}

	/**
	 * Handles actions from the Add/Save button
	 */
	void addAction() {
		switch(btnAdd.getText()) {
			case "Add":
				disableButtonsExcept(btnAdd);
				list.setEnabled(false);
				newBrickFields();
				unlockFields();
				break;
			
			case "Save":
				resetButtons();
				list.setEnabled(true);
				String newItem=nameTextArea.getText();
				lockFields();
				if(!newItem.equals("")) {
					brick newBrick=new brick(nameTextArea.getText(), documentationTextArea.getText(), functionTextArea.getText());
					bricksIO.appendBrickToFile(newBrick);
					updateBricksList();
					list.setSelectedIndex(getBrickIndex(newBrick));
				}
				updateDisplay();
				break;
		}
	}
	
	/**
	 * Handles actions from the Update/Save button
	 */
	void updateAction() {
		switch(btnUpdate.getText()) {
			case "Update":
				disableButtonsExcept(btnUpdate);
				list.setEnabled(false);
				unlockFields();
				break;
			
			case "Save":
				resetButtons();
				list.setEnabled(true);
				String newItem=nameTextArea.getText();
				lockFields();
				if(!newItem.equals("")) {
					brick newBrick=new brick(nameTextArea.getText(), documentationTextArea.getText(), functionTextArea.getText());
					updateBrick();
					updateBricksList();
					list.setSelectedIndex(getBrickIndex(newBrick));
				}
				updateDisplay();
				break;
		}
	}
	
	/**
	 * Handles actions from the Delete/Save button
	 */
	void deleteAction() {
		switch(btnDelete.getText()) {
			case "Delete":
				disableButtonsExcept(btnDelete);
				list.setEnabled(false);
				lockFields();
				break;
			
			case "Save":
				resetButtons();
				int currIndex=list.getSelectedIndex();
				list.setEnabled(true);
				lockFields();
				deleteBrick();
				updateBricksList();
				updateDisplay();
				if(list.getItemCount()>0) list.setSelectedIndex(Math.max(0, currIndex-1));
				break;
		}
	}
	
	/**
	 * Handles actions from the Push button
	 */
	void pushAction() {
		if(btnPush.isVisible()) {
			Prefs.set("recorder.mode", "Macro");
			if(Recorder.getInstance()==null) IJ.run("Record...", "");
			Recorder.recordString("\n"+"//----------   "+nameTextArea.getText()+"   ----------"+"\n");
			Recorder.recordString(functionTextArea.getText()+"\n\n");
		}
	}
	
	/**
	 * Handles actions from the Close/Cancel button
	 */
	void closeAction() {
		switch(btnClose.getText()) {
			case "Close":
				if(showPush) {
					this.setVisible(false);
				}else {
					WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
					Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
				}
			    break;
			case "Cancel":
				resetButtons();
				list.setEnabled(true);
				lockFields();
				updateDisplay();
				break;
		}
	}
	
	/**
	 * Update the display
	 */
	void updateDisplay() {
		btnPush.setVisible(showPush);
		if(showPush) setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		brick selectedBrick=list.getSelectedIndex()!=-1 && bricks.size()>0?bricks.get(list.getSelectedIndex()):null;
		emptyFields();
		if(selectedBrick!=null) {
			nameTextArea.setText(selectedBrick.getName());
			nameTextArea.setCaretPosition(0);
			documentationTextArea.setText(selectedBrick.getDocumentation());
			documentationTextArea.setCaretPosition(0);
			functionTextArea.setText(selectedBrick.getFunction());
			functionTextArea.setCaretPosition(0);
		}
		resetButtons();
	}
	
	/**
	 * Update a single brick and saves its new version to the output file
	 */
	void updateBrick() {
		brick updatedBrick=new brick(nameTextArea.getText(), documentationTextArea.getText(), functionTextArea.getText());
		int brickIndex=list.getSelectedIndex();
		if(brickIndex!=-1) bricks.set(brickIndex, updatedBrick);
		bricksIO.writeBricksFile(bricks);
	}
	
	/**
	 * Removes the selected brick from both the displayed list and from the file
	 */
	void deleteBrick() {
		int brickIndex=list.getSelectedIndex();
		if(brickIndex!=-1) bricks.remove(brickIndex);
		bricksIO.writeBricksFile(bricks);
	}
	
	/**
	 * Updates the bricks list from the file, and updates the JComboBox's model
	 */
	void updateBricksList() {
		bricks=bricksIO.readBrickFile();
		list.setModel(new DefaultComboBoxModel<String>(bricksIO.getBricksNames()));
	}
	
	/**
	 * Finds the index of the target brick within the bricks list
	 * @param target the target brick to look for
	 * @return the brick's index in the list or -1 if not found
	 */
	int getBrickIndex(brick target) {
		for(int i=0; i<bricks.size(); i++) {
			brick currBrick=bricks.get(i);
			if(currBrick.getName().equals(target.getName()) && currBrick.getDocumentation().equals(target.getDocumentation()) && currBrick.getFunction().equals(target.getFunction())) return i;
		}
		return -1;
	}
	
	/**
	 * Empties the fields where the bricks' attributes are displayed
	 */
	public void emptyFields() {
		nameTextArea.setText("");
		documentationTextArea.setText("");
		functionTextArea.setText("");
	}
	
	/**
	 * Empties the fields where the bricks' attributes are displayed
	 */
	public void newBrickFields() {
		nameTextArea.setText("Place the function's title here");
		
		documentationTextArea.setText(	"**Description**\n\n" + 
										"Place your description here.\n\n" +
										"**Requirement**\n\n" + 
										"Tell here which plugins are required to run the function. -OR- None, this function uses ImageJ's built-in functions.\n\n" + 
										
										"**Parameters**\n\n" + 
										"* *Param1*: Description of the first parameter.\n" + 
										"* *Param2*: Description of the second parameter.\n" + 
										"\n" + 
										"**Output**\n\n" + 
										"A description of the function's output.");
		
		functionTextArea.setText(		"function myFunction(param1, param2){\n" + 
										"	//Place some code here\n" + 
										"	return output;\n" + 
										"}");
	}
	
	/**
	 * Sets all the input fields as not editable
	 */
	public void lockFields() {
		setFieldsStatus(false);
	}
	
	/**
	 * Sets all the input fields as editable
	 */
	public void unlockFields() {
		setFieldsStatus(true);
	}
	
	/**
	 * Sets all the input fields as (not) editable, depending on the input status
	 * @param status the status to apply to all fields, as a boolean
	 */
	void setFieldsStatus(boolean status) {
		nameTextArea.setEditable(status);
		documentationTextArea.setEditable(status);
		functionTextArea.setEditable(status);
	}
	
	/**
	 * Resets the feel and look of all buttons
	 */
	void resetButtons() {
		btnAdd.setText("Add");
		btnAdd.setForeground(Color.BLACK);
		btnAdd.setEnabled(true);
		
		btnUpdate.setText("Update");
		btnUpdate.setForeground(Color.BLACK);
		btnUpdate.setEnabled(bricks.size()>0);
		
		btnDelete.setText("Delete");
		btnDelete.setForeground(Color.BLACK);
		btnDelete.setEnabled(bricks.size()>0);
		
		btnPush.setText("Push");
		btnPush.setForeground(Color.BLACK);
		btnPush.setEnabled(bricks.size()>0);
		
		btnClose.setText("Close");
		btnClose.setForeground(Color.BLACK);
		btnClose.setEnabled(true);
	}
	
	/**
	 * Disables all buttons, except the one provided as argument.
	 * Changes the "Close" button to a "Cancel" button
	 * @param btn the button to keep enabled
	 */
	void disableButtonsExcept(JButton btn) {
		btnAdd.setEnabled(false);
		btnUpdate.setEnabled(false);
		btnDelete.setEnabled(false);
		btnPush.setEnabled(false);
		
		btn.setEnabled(true);
		btn.setText("Save");
		btn.setForeground(Color.RED);
		
		btnClose.setEnabled(true);
		btnClose.setText("Cancel");
		btnClose.setForeground(Color.RED);
	}
}
