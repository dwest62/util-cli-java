/**
 * Functional interface designed to be used by CLIMenu to provide custom MenuItem formatting.
 *
 * @author James West
 * @version 1.0
 */
@FunctionalInterface
public interface MenuItemFormatter {
	/**
	 * @param index       The index of the MenuItem in the menu.
	 * @param delimiter   The delimiter set for MenuItem numbering.
	 * @param description The description of the MenuItem
	 * @return The String representing a MenuItem for display in the menu.
	 */
	String format(int index, String delimiter, String description);
}
