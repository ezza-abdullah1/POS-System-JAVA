CREATE TABLE Users (
    UserID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    Password VARCHAR(255) NOT NULL,
    Role ENUM(
        'SuperAdmin',
        'BranchManager',
        'Cashier',
        'DataEntryOperator'
    ) NOT NULL,
    BranchCode INT NULL,
    Salary DECIMAL(10, 2) NULL,
    EmpNumber INT UNIQUE NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE TABLE Branches (
    BranchID INT AUTO_INCREMENT PRIMARY KEY,
    BranchCode INT UNIQUE NOT NULL,
    BranchName VARCHAR(100) NOT NULL,
    City VARCHAR(100) NOT NULL,
    Address VARCHAR(255) NOT NULL,
    Phone VARCHAR(20),
    NumEmployees INT DEFAULT 0,
    IsActive BOOLEAN DEFAULT TRUE,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE TABLE Reports (
    ReportID INT AUTO_INCREMENT PRIMARY KEY,
    BranchCode INT NOT NULL,
    ReportType ENUM('Sales', 'Stock', 'Profit') NOT NUmLL,
    DateFrom DATE NOT NULL,
    DateTo DATE NOT NULL,
    GeneratedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (BranchCode) REFERENCES Branches(BranchCode) ON DELETE CASCADE
);
CREATE TABLE Cards (
    card_id INT NOT NULL AUTO_INCREMENT,
    card_number VARCHAR(16) NOT NULL UNIQUE,
    security_code VARCHAR(4) NOT NULL,
    balance DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (card_id)
);
CREATE TABLE Products (
    product_id VARCHAR(10) NOT NULL,
    name VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    tax DECIMAL(5, 2) NOT NULL,
    weight DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (product_id)
);
CREATE TABLE Customers (
    customer_key INT NOT NULL,
    holder_name VARCHAR(100),
    points INT DEFAULT 0,
    date_issued DATE,
    metCrdNum VARCHAR(50),
    PRIMARY KEY (customer_key)
);
CREATE TABLE Billing (
    id INT NOT NULL AUTO_INCREMENT,
    subtotal DECIMAL(10, 2) NOT NULL,
    discount DECIMAL(10, 2) NOT NULL,
    tax DECIMAL(10, 2) NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    metro_card VARCHAR(255),
    payment_method VARCHAR(20),
    PRIMARY KEY (id)
);