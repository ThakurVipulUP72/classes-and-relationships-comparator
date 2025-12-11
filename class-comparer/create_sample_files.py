import openpyxl
from openpyxl import Workbook

# Create Manual XLSX file
wb_manual = Workbook()
ws_manual = wb_manual.active
ws_manual.title = "Classes"

# Add headers
ws_manual['A1'] = 'Story'
ws_manual['B1'] = 'Class'

# Sample data for manually created classes
manual_data = [
    ('User Authentication', 'User'),
    ('User Authentication', 'AuthenticationService'),
    ('User Authentication', 'LoginController'),
    ('User Authentication', 'PasswordValidator'),
    ('Shopping Cart', 'Cart'),
    ('Shopping Cart', 'CartItem'),
    ('Shopping Cart', 'CartService'),
    ('Shopping Cart', 'PriceCalculator'),
    ('Payment Processing', 'Payment'),
    ('Payment Processing', 'PaymentGateway'),
    ('Payment Processing', 'Transaction'),
    ('Payment Processing', 'PaymentValidator'),
    ('Order Management', 'Order'),
    ('Order Management', 'OrderService'),
    ('Order Management', 'OrderRepository'),
    ('Order Management', 'OrderStatus'),
]

# Add data rows
for idx, (story, class_name) in enumerate(manual_data, start=2):
    ws_manual[f'A{idx}'] = story
    ws_manual[f'B{idx}'] = class_name

# Save manual file
wb_manual.save('manual_classes.xlsx')
print("Created: manual_classes.xlsx")

# Create LLM-generated XLSX file
wb_llm = Workbook()
ws_llm = wb_llm.active
ws_llm.title = "Classes"

# Add headers
ws_llm['A1'] = 'Story'
ws_llm['B1'] = 'Class'

# Sample data for LLM-generated classes (some overlap, some different)
llm_data = [
    ('User Authentication', 'User'),
    ('User Authentication', 'AuthenticationService'),
    ('User Authentication', 'LoginController'),
    ('User Authentication', 'TokenGenerator'),  # Different from manual
    ('User Authentication', 'SessionManager'),   # Different from manual
    ('Shopping Cart', 'Cart'),
    ('Shopping Cart', 'CartItem'),
    ('Shopping Cart', 'CartService'),
    ('Shopping Cart', 'DiscountCalculator'),     # Different from manual
    ('Payment Processing', 'Payment'),
    ('Payment Processing', 'PaymentGateway'),
    ('Payment Processing', 'Transaction'),
    ('Payment Processing', 'CreditCardProcessor'), # Different from manual
    ('Order Management', 'Order'),
    ('Order Management', 'OrderService'),
    ('Order Management', 'OrderRepository'),
    ('Order Management', 'Shipment'),            # Different from manual
    ('Order Management', 'Invoice'),             # Different from manual
]

# Add data rows
for idx, (story, class_name) in enumerate(llm_data, start=2):
    ws_llm[f'A{idx}'] = story
    ws_llm[f'B{idx}'] = class_name

# Save LLM file
wb_llm.save('llm_classes.xlsx')
print("Created: llm_classes.xlsx")

print("\nSample files created successfully!")
print("- manual_classes.xlsx: 16 classes across 4 stories")
print("- llm_classes.xlsx: 18 classes across 4 stories")
