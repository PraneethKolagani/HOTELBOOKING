SQL-Database with Core Java integration hotel booking management system.

  Hotel Booking Management System (Core Java + MySQL)

A **console-based Hotel Booking Management System** developed in **Core Java** with **MySQL database integration**. This system allows customers to register, search hotels by city, view available rooms, book hotels, make payments, cancel bookings, and more.

---

 Technologies Used

*  **Java (Core Java - JDBC)**
*  **MySQL (phpMyAdmin or MySQL Workbench)**
*  **JDBC (Java Database Connectivity)**
*  **Command-Line Interface (CLI)**

---

 Features

*  **User Registration**
*  **Search Hotels by City**
*  **List Available Hotels**
*  **Show Available Rooms**
*  **Select Check-In / Check-Out Dates**
*  **Book Rooms**
*  **Simulated Payment Gateway**
*  **View All Bookings**
*  **Cancel Bookings**
*  **User Logout**

---

 Database Structure (MySQL)

Database Name: `hotel_booking`

**Tables:**

* `customers(id, name, email, phone)`
* `hotels(id, name, city)`
* `rooms(id, hotel_id, room_number, type, is_booked)`
* `bookings(id, customer_id, hotel_id, room_id, check_in, check_out, status)`
* `payments(id, booking_id, amount, payment_date, status)`


---

##  How to Run

1. Install **MySQL** and create the `hotel_booking` database.
2. Import the required tables using the provided SQL file or your schema.
3. Add the **MySQL JDBC Driver** to your Java project classpath (e.g., `mysql-connector-java.jar`).
4. Compile and run `HotelBooking.java`.
5. Interact through the command-line prompts.

---

##  Screenshots

![{9DC21DB9-8EEC-4D8B-ADAC-BAB64825DACC}](https://github.com/user-attachments/assets/f3783838-afbd-457e-90e2-466527a99049)


https://github.com/user-attachments/assets/7442eab6-503c-40b8-a304-4414f63ec99e


---

## 🚀 Future Enhancements

* Convert to **Java Swing GUI**
* Create **Android APK** version
* Integrate real **Razorpay or Paytm** payment gateway
* Email invoice receipts
* Admin panel for managing hotels & bookings

---

## 🧑‍💻 Author

**Praneeth Kolagani**

Core Java Developer | Database Integrator
India 🇮🇳

---

## 📜 License

This project is open source under the [MIT License](LICENSE).


