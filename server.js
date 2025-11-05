const express = require("express");
const app = express();
const mysql = require("mysql2");
const cors = require("cors");

app.use(cors());                // ✅ allow requests from Android
app.use(express.json());        // ✅ parse JSON body

const db = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "root",
  database: "madprjdb"
});

db.connect(err => {
  if (err) console.log("DB Connection Error:", err);
  else console.log("Connected to MySQL");
});

app.post("/login", (req, res) => {
  console.log("Login request received:", req.body); // 👈 should now show the data

  const { email, password } = req.body;
  if (!email || !password)
    return res.status(400).json({ success: false, message: "Missing fields" });

  const sql = "SELECT * FROM users WHERE email = ? AND password = ?";
  db.query(sql, [email, password], (err, results) => {
    if (err) return res.status(500).json({ success: false, message: "DB error" });
    if (results.length > 0)
      res.json({ success: true, message: "Login successful" });
    else
      res.json({ success: false, message: "Invalid credentials" });
  });
});

app.get("/getUserData", (req, res) => {
  const email = req.query.email;
  const sql = "SELECT * FROM users WHERE email = ?";

  db.query(sql, [email], (err, results) => {
    if (err) {
      console.error(err);
      return res.status(500).json({ success: false, message: "Database error" });
    }
    if (results.length === 0) {
      return res.status(404).json({ success: false, message: "User not found" });
    }

    res.json({ success: true, user: results[0] });
  });
});

app.post("/signup", (req, res) => {
  console.log("Signup request received:", req.body);

  const {
    name,
    email,
    password,
    age,
    weight,
    gender,
    height,
    HG_DailyCal,
    HG_Sleep,
    HG_Water,
    HG_Steps,
    MI_BloodType,
    MI_Allergies,
    MI_MedicalCondition
  } = req.body;

  // ✅ Basic field validation
  if (!name || !email || !password) {
    return res.status(400).json({
      success: false,
      message: "Missing required fields (name, email, password)"
    });
  }

  // ✅ SQL query
  const sql = `
    INSERT INTO users (
      name, email, password, age, weight, gender, height,
      HG_DailyCal, HG_Sleep, HG_Water, HG_Steps,
      MI_BloodType, MI_Allergies, MI_MedicalCondition
    )
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
  `;

  const values = [
    name,
    email,
    password,
    age || null,
    weight || null,
    gender || null,
    height || null,
    HG_DailyCal || null,
    HG_Sleep || null,
    HG_Water || null,
    HG_Steps || null,
    MI_BloodType || null,
    MI_Allergies || null,
    MI_MedicalCondition || null
  ];

  db.query(sql, values, (err, result) => {
    if (err) {
      console.error("Error inserting user:", err);
      return res.status(500).json({
        success: false,
        message: "Database insertion failed",
        error: err
      });
    }

    console.log("User inserted with ID:", result.insertId);
    res.json({
      success: true,
      message: "User registered successfully",
      userId: result.insertId
    });
  });
});

// ✅ Store calorie intake for a user
app.post("/addCalorieIntake", (req, res) => {
  const { email, calorie_intake } = req.body;

  if (!email || !calorie_intake) {
    return res.status(400).json({
      success: false,
      message: "Missing required fields (email, calorie_intake)"
    });
  }

  const sql = "INSERT INTO user_calorie_intake (email, calorie_intake) VALUES (?, ?)";
  db.query(sql, [email, calorie_intake], (err, result) => {
    if (err) {
      console.error("Error inserting calorie intake:", err);
      return res.status(500).json({ success: false, message: "Database error" });
    }

    res.json({
      success: true,
      message: "Calorie intake stored successfully",
      id: result.insertId
    });
  });
});

// ✅ Get all user activity + calorie data for AI report
app.get("/getUserFullData", (req, res) => {
  const { email } = req.query;
  if (!email) return res.status(400).json({ success: false, message: "Missing email" });

  const sqlUser = "SELECT * FROM users WHERE email = ?";
  const sqlCalories = "SELECT calorie_intake, timestamp FROM user_calorie_intake WHERE email = ? ORDER BY timestamp DESC LIMIT 10";

  // Fetch both user info and calorie data
  db.query(sqlUser, [email], (err, userResults) => {
    if (err) return res.status(500).json({ success: false, message: "DB error (user)" });
    if (userResults.length === 0) return res.status(404).json({ success: false, message: "User not found" });

    db.query(sqlCalories, [email], (err2, calorieResults) => {
      if (err2) return res.status(500).json({ success: false, message: "DB error (calories)" });

      res.json({
        success: true,
        user: userResults[0],
        calories: calorieResults
      });
    });
  });
});

app.listen(3000, () => console.log("Server running on port 3000"));