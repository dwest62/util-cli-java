/**
 * A menu item containing a description to be displayed on the menu and an action to be taken when this menu item is
 * chosen.
 *
 * @param description The description of the menu item to be displayed on the menu.
 * @param action The action to be taken when this menu item is chosen.
 *
 * @author James West
 * @version 1.0
 */
record MenuItem(String description, Runnable action) {
	@Override
	public String toString(){
		return this.description;
	}
}
