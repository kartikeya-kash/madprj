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

app.listen(3000, () => console.log("Server running on port 3000"));