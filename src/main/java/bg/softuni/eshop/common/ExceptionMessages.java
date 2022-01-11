package bg.softuni.eshop.common;

public class ExceptionMessages {

    private ExceptionMessages() {}

    public static final String USER_NOT_FOUND = "User with username %s was not found";

    public static final String INVALID_USERNAME_MESSAGE = "Username must be between 2 and 20 symbols.";

    public static final String INVALID_PASSWORD_MESSAGE = "Password must be between 2 and 15 symbols.";

    public static final String INVALID_PRODUCT_TYPE = "Invalid product type.";

    public static final String PRODUCT_NOT_FOUND_MESSAGE = "Product not found";

    public static final String PRODUCT_ALREADY_EXISTS_MESSAGE = "Product with id %s already exists.";

    public static final String ROLE_NOT_FOUND_MESSAGE = "Role %s not found.";

    public static final String NO_ACTIVE_ORDER_MESSAGE = "There is no active order";

    public static final String NON_EXISTING_ORDER_ENTITY_MESSAGE = "Order item with id:(%s) does not exist";

    public static final String INVALID_ORDER_ITEM_ID_MESSAGE = "Invalid order item id.";

    public static final String INVALID_PRODUCT_ID_MESSAGE = "Invalid product item id.";

    public static final String NON_EXISTING_ORDER_ITEM = "Order item with id %s does not exist.";

    public static final String PASSWORDS_MISMATCH_MESSAGE = "Password confirmation failed. Passwords do not match";

    public static final String EMAIL_MISMATCH_MESSAGE = "Email confirmation failed. Emails do not match";

    public static final String INVALID_EMAIL_MESSAGE = "Please, enter valid email";

    public static final String USER_ALREADY_EXIST_MESSAGE = "User already exists!";

    public static final String USER_EMAIL_ALREADY_EXIST_MESSAGE = "User e-mail already exists!";

    public static final String COMMENT_LENGTH_MESSAGE = "Your review should not exceed 300 characters.";
}
