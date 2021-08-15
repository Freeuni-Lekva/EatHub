package ge.eathub.mailer.mails;


import ge.eathub.dto.OrderDto;

import java.math.BigDecimal;
import java.util.List;

public class OrderMail extends Mail {
    private final static String SUBJECT = "Order received";
    private final static String MESSAGE = """
              <html>
              <meta charset="utf-8">
              <body>
                 <p> your order has been received in room - %s </p>
                 <p> Rest - %s | Loc - %s | Time - %s  </p> 
                  <div> 
                    <p>your orders</p>
                    %s
                  </div>
                  
                   <p>amount %s GEL</p>
                   
              </body>
             </html>
            """;

    public OrderMail(String email, Long roomID, List<OrderDto> orders, String restaurantName,
                     String location, String time) {
        super(email, SUBJECT);
        StringBuilder sb = new StringBuilder();
        BigDecimal amount = generateOrderList(sb, orders);
        setMessage(MESSAGE.formatted(roomID, restaurantName, location, time, sb.toString(), amount));
    }

    public OrderMail(String email, Long roomID, List<OrderDto> orders, String restaurantName, String location,
                     String time, BigDecimal amount) {
        super(email, SUBJECT);
        StringBuilder sb = new StringBuilder();
        generateOrderList(sb, orders);
        setMessage(MESSAGE.formatted(roomID, restaurantName, location, time, sb.toString(), amount));
    }


    private static BigDecimal generateOrderList(StringBuilder sb, List<OrderDto> orders) {
        final BigDecimal[] amount = {new BigDecimal(0)};
        sb.append("<table>");
        sb.append("<tr><th>Meal</th><th>Amount</th><th>Cooking Time</th><th>Price</th></tr>");
        orders.forEach(order -> {
            sb.append("<tr>");

            appendCell(sb, order.getMealName());
            appendCell(sb, order.getQuantity().toString());
            appendCell(sb, order.getCookingTime().toString());
            appendCell(sb, order.getTotalPrice().toString());

            sb.append("</tr>");
            amount[0] = amount[0].add(order.getTotalPrice());
        });
        sb.append("</table>");
        return amount[0];
    }

    private static void appendCell(StringBuilder sb, String value) {
        sb.append("<td>");
        sb.append(value);
        sb.append("</td>");
    }

}

