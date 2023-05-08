/**
 * A menu item containing a description to be displayed on the menu and an action to be taken when this menu item is
 * chosen.
 *
 * @param description The description of the menu item to be displayed on the menu.
 * @param action The action to be taken when this menu item is chosen.
 */
record MenuItem(String description, Runnable action) {}
