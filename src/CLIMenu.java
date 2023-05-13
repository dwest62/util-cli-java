import java.util.*;
import java.util.function.Predicate;

/**
 * Represents a Command Line Interface (CLI) Menu.
 *
 * @author James West
 * @version 1.0
 */
public class CLIMenu {

    private final ArrayList<MenuItem> menuItems;

    private final String welcome;

    private final String prompt;
    private final MenuItem exitOption;
    private final InputErrorHandler invalidIntegerChoiceHandler;
    private final InputErrorHandler outOfBoundsChoiceHandler;
    private final String delimiter;

    private final MenuItemFormatter menuItemFormatter;
    /**
     * Protected constructor CLIMenu used by builder to create a new CLImenu instance.
     *
     * @param builder The Builder object containing menu configuration.
     */
    protected CLIMenu(Builder builder) {
        this.menuItems = builder.menuItems;
        this.welcome = builder.welcome;
        this.prompt = builder.prompt;
        this.exitOption = builder.exit;
        this.invalidIntegerChoiceHandler = builder.invalidIntegerChoiceHandler;
        this.outOfBoundsChoiceHandler = builder.outOfBoundsChoiceHandler;
        this.delimiter = builder.delimiter;
        this.menuItemFormatter = builder.menuItemFormatter;
    }

    /**
     * Build a printable, formatted CLI menu from this menu.
     *
     * @return String containing a formatted CLI menu.
     */
    public String getFormattedMenu() {
        StringBuilder sb = new StringBuilder(this.welcome).append("\n");
        for (int i = 0; i < menuItems.size(); i++) {
            sb.append(menuItemFormatter.format(i + 1, this.delimiter, String.valueOf(menuItems.get(i))));
        }
        return sb.append(prompt).toString();
    }


    /**
     * Runs the menu, prompting the user for input and executing the corresponding action until the exit option is
     * chosen.
     *
     * @param scanner The scanner used to get input from the user.
     */
    public void runMenu(Scanner scanner) {
        Predicate<Integer> isNotExitOption = choice -> choice != menuItems.indexOf(exitOption) + 1;
        Predicate<Integer> isValidIndex = choice -> choice > 0 && choice <= menuItems.size();
        String prompt = this.getFormattedMenu();
        TryParse<Integer> parser = TryParse.forInteger();
        Rule<Integer> rule = new Rule<>(isValidIndex, outOfBoundsChoiceHandler);

        int choice;
        do {
            choice = InputHelper.requestValidInput(scanner, prompt, invalidIntegerChoiceHandler, parser, rule);
            this.menuItems.get(choice - 1).action().run();
        } while (isNotExitOption.test(choice));
    }


    /**
     * Builder class for construction CLIMenu instances.
     */
    public static class Builder {
        private final ArrayList<MenuItem> menuItems;
        private String welcome = "Welcome to the menu!";
        private String prompt = "Enter your choice: ";
        private MenuItem exit =
                new MenuItem("Exit", () -> System.out.println("Exiting the menu..."));
        private InputErrorHandler invalidIntegerChoiceHandler;
    
        private InputErrorHandler outOfBoundsChoiceHandler;
    
        private String delimiter = ".";
        
        private MenuItemFormatter menuItemFormatter =
            (ordinal, delimiter, description) -> String.format("\t%d%s %s\n", ordinal, delimiter, description);
        
        /**
         * Constructs a Builder with an ArrayList of MenuItem objects.
         */
        public Builder() {
            this.menuItems = new ArrayList<>();
        }
    
        /**
         * Sets the welcome message for the menu.
         *
         * @param welcome   The welcome message to display.
         * @return The Builder instance for method chaining.
         */
        public Builder welcome(String welcome) {
            this.welcome = welcome;
            return this;
        }
    
        /**
         * Sets the prompt message for the menu.
         *
         * @param prompt    The prompt message to display.
         * @return The Builder instance for method chaining.
         */
        public Builder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }
    
        /**
         * Sets the delimiter for the menu item numbering.
         *
         * @param delimiter     The delimiter to use.
         * @return The Builder instance for method chaining.
         */
        public Builder delimiter(String delimiter) {
            this.delimiter = delimiter;
            return this;
        }
    
        /**
         * Adds a menu item to the menu.
         *
         * @param option    The menu item to add.
         * @return The Builder instance for method chaining.
         */
        public Builder addMenuItem(MenuItem option) {
            this.menuItems.add(option);
            return this;
        }
    
        /**
         * Creates a new menu item from the description and action inputs and adds the menu item to the menu
         *
         * @param description   The description of the menu item.
         * @param action        The action to execute when the menu item is selected.
         * @return The Builder instance for method chaining.
         */
        public Builder addMenuItem(String description, Runnable action) {
            this.menuItems.add(new MenuItem(description, action));
            return this;
        }
    
        /**
         * Creates a new menu item from the description and action inputs and adds the menu item to the menu at the
         * given ordinal.
         *
         * The ordinal is indexed at 1.
         *
         * @param ordinal       The ordinal at which the menu item will be placed. Indexed at 1.
         * @param description   The description of the menu item.
         * @param action        The action to execute when the menu item is selected.
         * @return The Builder instance for method chaining.
         */
        public Builder addMenuItem(int ordinal, String description, Runnable action) {
            this.menuItems.add(ordinal - 1, new MenuItem(description, action));
            return this;
        }
    
        /**
         * Adds a list of menu items to the menu.
         *
         * @param menuItems     The menu items to add to the menu.
         * @return The Builder instance for method chaining.
         */
        public Builder addMenuItems(List<MenuItem> menuItems) {
            this.menuItems.addAll(menuItems);
            return this;
        }
    
        /**
         * Sets the exit option for the menu. When the user inputs this options number, the menu loop will terminate,
         * and the program will exit the menu.
         *
         * @param exit  The MenuItem that represents the exit option.
         * @return The Builder instance for method chaining.
         */
        public Builder exit(MenuItem exit) {
            this.exit = exit;
            return this;
        }
    
        /**
         * Sets the formatter to be used to add custom menu item formatting.
         *
         * @param formatter     The Formatter used to add custom menu item formatting.
         * @return The Builder instance for method chaining.
         */
        public Builder menuItemFormatter(MenuItemFormatter formatter) {
            this.menuItemFormatter = formatter;
            return this;
        }
    
        /**
         * Sets the InputErrorHandler to be used when the user choice is out of bounds of the menu.
         *
         * @param outOfBoundsChoiceHandler  The InputErrorHandler to be used when the user choice is out of bounds of
         *                                 the menu.
         * @return The Builder instance for method chaining.
         */
        public Builder outOfBoundsChoiceHandler(InputErrorHandler outOfBoundsChoiceHandler) {
            this.outOfBoundsChoiceHandler = outOfBoundsChoiceHandler;
            return this;
        }
    
    
        /**
         * Sets the InputErrorHandler to be used when the user input is not parsable to an Integer.
         *
         * @param invalidIntegerChoiceHandler   The InputErrorHandler to be used when the user choice is out of bounds
         *                                    of the menu.
         * @return The Builder instance for method chaining.
         */
        public Builder invalidIntegerChoiceHandler(InputErrorHandler invalidIntegerChoiceHandler) {
            this.invalidIntegerChoiceHandler = invalidIntegerChoiceHandler;
            return this;
        }

        /**
         * Builds a CLIMenu instance using the current configuration.
         *
         * @return A new CLIMenu instance.
         */
        public CLIMenu build() {
            this.addMenuItem(exit);
            if(this.invalidIntegerChoiceHandler == null) {
                this.invalidIntegerChoiceHandler =
                    (input) -> System.out.println("Failed to parse input, please try again.");
            }
            if(this.outOfBoundsChoiceHandler == null) {
                this.outOfBoundsChoiceHandler =
                    (input) -> System.out.println(
                        "Failed to run choice " + input + ", please enter a number between 1 and " + menuItems.size()
                    );
            }
            return new CLIMenu(this);
        }
    }
}

