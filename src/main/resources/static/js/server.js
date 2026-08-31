const express = require("express")
const app = express()
const cors = require("cors")
app.use(
    cors({
        origin: "http://127.0.0.1:ffff"
    })
)

app.get("/data", (req, res) => {
  res.json({"cpu_usage": "60%"})  
})

app.listen(8000)