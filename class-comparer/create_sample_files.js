const XLSX = require('xlsx');

// Create Manual workbook
const manualData = [
    ['Story', 'Class'],
    ['User Authentication', 'User'],
    ['User Authentication', 'AuthenticationService'],
    ['User Authentication', 'LoginController'],
    ['User Authentication', 'PasswordValidator'],
    ['Shopping Cart', 'Cart'],
    ['Shopping Cart', 'CartItem'],
    ['Shopping Cart', 'CartService'],
    ['Shopping Cart', 'PriceCalculator'],
    ['Payment Processing', 'Payment'],
    ['Payment Processing', 'PaymentGateway'],
    ['Payment Processing', 'Transaction'],
    ['Payment Processing', 'PaymentValidator'],
    ['Order Management', 'Order'],
    ['Order Management', 'OrderService'],
    ['Order Management', 'OrderRepository'],
    ['Order Management', 'OrderStatus'],
];

const manualWS = XLSX.utils.aoa_to_sheet(manualData);
const manualWB = XLSX.utils.book_new();
XLSX.utils.book_append_sheet(manualWB, manualWS, 'Classes');
XLSX.writeFile(manualWB, 'manual_classes.xlsx');
console.log('Created: manual_classes.xlsx');

// Create LLM workbook
const llmData = [
    ['Story', 'Class'],
    ['User Authentication', 'User'],
    ['User Authentication', 'AuthenticationService'],
    ['User Authentication', 'LoginController'],
    ['User Authentication', 'TokenGenerator'],
    ['User Authentication', 'SessionManager'],
    ['Shopping Cart', 'Cart'],
    ['Shopping Cart', 'CartItem'],
    ['Shopping Cart', 'CartService'],
    ['Shopping Cart', 'DiscountCalculator'],
    ['Payment Processing', 'Payment'],
    ['Payment Processing', 'PaymentGateway'],
    ['Payment Processing', 'Transaction'],
    ['Payment Processing', 'CreditCardProcessor'],
    ['Order Management', 'Order'],
    ['Order Management', 'OrderService'],
    ['Order Management', 'OrderRepository'],
    ['Order Management', 'Shipment'],
    ['Order Management', 'Invoice'],
];

const llmWS = XLSX.utils.aoa_to_sheet(llmData);
const llmWB = XLSX.utils.book_new();
XLSX.utils.book_append_sheet(llmWB, llmWS, 'Classes');
XLSX.writeFile(llmWB, 'llm_classes.xlsx');
console.log('Created: llm_classes.xlsx');

console.log('\nSample files created successfully!');
console.log('- manual_classes.xlsx: 16 classes across 4 stories');
console.log('- llm_classes.xlsx: 18 classes across 4 stories');
console.log('\nExpected comparison results:');
console.log('  Common classes: User, AuthenticationService, LoginController, Cart, CartItem, CartService, Payment, PaymentGateway, Transaction, Order, OrderService, OrderRepository');
console.log('  Only in Manual: PasswordValidator, PriceCalculator, PaymentValidator, OrderStatus');
console.log('  Only in LLM: TokenGenerator, SessionManager, DiscountCalculator, CreditCardProcessor, Shipment, Invoice');
