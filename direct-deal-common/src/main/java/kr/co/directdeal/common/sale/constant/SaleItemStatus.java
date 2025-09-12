package kr.co.directdeal.common.sale.constant;

/**
 * Enum representing the various statuses of a sale item.
 * <ul>
 *   <li><b>SALE</b> - The item is currently available for sale.</li>
 *   <li><b>STOPPED</b> - The sale of the item has been temporarily stopped.</li>
 *   <li><b>DELETED</b> - The item has been deleted and is no longer available.</li>
 *   <li><b>COMPLETED</b> - The sale of the item has been completed successfully.</li>
 * </ul>
 *
 * @author Cheol Jeon
 */
public enum SaleItemStatus {
    SALE,
    STOPPED,
    DELETED,
    COMPLETED
}
