# 🧾 Point of Sale (POS) System
---

## 🚀 Overview  
This POS (Point of Sale) System is a multi-role application designed for retail branches with layered user access: **Super Admin**, **Branch Manager**, **Cashier**, and **Data Entry Operator**. It manages user authentication, product entry, vendor details, sales processing, and generates graphical reports.

---

## 🖥️ Screens & Workflow

### 🔵 First Screen – Splash Window  
- Loading screen on application start

---

### 🔐 Second Screen – Login Panel  
Buttons for login as:  
- **Super Admin**  
- **Admin / Branch Manager**  
- **Cashier**  
- **Data Entry Operator**

---

## 👤 Role-Based Functionality

---

### 👑 Login as Super Admin

#### Features:
- **Create Branch**
  - Branch ID / Code
  - Name / City
  - Address
  - Phone
  - Set Active (true)
  - Initial Employee Count = 0
- **Create Branch Manager** for each branch

#### 📊 Reports Module:
- Generate reports by:
  - **Branch Code**
  - **Time Frame**: Today / Weekly / Monthly / Yearly / Custom Range  
- Report Types:
  - **Sales**
  - **Remaining Stock**
  - **Profit**

#### 📈 Graphs/Charts:
- Visual charts for:
  - Sales trends
  - Remaining stock
  - Profit metrics

---

### 🧑‍💼 Login as Admin / Branch Manager

#### On First Login:
- **Change password** for all employees (new + confirm)

#### Branch Manager Features:
- Add employees:
  - **Cashier**
  - **Data Entry Operator**
- Set:
  - Name
  - Employee Number
  - Email
  - Default Password: `Password_123`
  - Branch Code
  - Salary

---

### 🧾 Login as Data Entry Operator

#### On First Login:
- Prompt to change default password

#### Attributes:
- Name  
- Employee No.  
- Email  
- Password (default: `Password_123`)  
- Branch Code  
- Salary  

#### 📦 Vendor Management:
- **Old Vendor**: Fetch and display vendor info
- **New Vendor**: Add new vendor with required attributes

#### 📦 Product Entry:
- Click ➕ to open product entry panel
- Fields include:
  - Product Name
  - Category
  - Original Price
  - Sale Price
  - Price by unit
  - Price by carton
- **Save Button**: Save product info to DB

---

### 💵 Login as Cashier

#### Features:
- Enter purchased product details
- Generate bill
- Subtract purchased items from DB after clicking **OK**

---

## 📂 Tech Notes

- Multi-role system with clear access levels
- Password reset required for first-time login
- Real-time product and sales handling
- Visual reports and analytics 

---

## ✅ Completion Criteria

- Working login and user creation system  
- Product and vendor management  
- Sales and reporting modules  
- Password reset for first-time login users  
- Graphical reports 

---

