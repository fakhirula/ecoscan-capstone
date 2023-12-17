const express = require("express");
const TeachableMachine = require("@sashido/teachablemachine-node");

const model = new TeachableMachine({
  modelUrl: "https://teachablemachine.withgoogle.com/models/r_FJjjBKN/"
});

const app = express();
const port = 8000;

app.get("/image/classify", async (req, res) => {
  const { url } = req.query;

  if (!url || typeof url !== 'string') {
    return res.status(400).send("Invalid or missing url parameter!");
  }

  return model.classify({
    imageUrl: url,
  }).then((predictions) => {
    console.log(predictions);
    return res.json(predictions);
  }).catch((e) => {
    console.error(e);
    res.status(500).send("Something went wrong!")
  });
});

app.listen(port, () => {
  console.log(`Example app listening at http://localhost:${port}`);
});



// const express = require("express");
// const TeachableMachine = require("@sashido/teachablemachine-node");

// const model = new TeachableMachine({
//   modelUrl: "https://teachablemachine.withgoogle.com/models/r_FJjjBKN/"
// });

// const app = express();
// const port = 8000;

// app.get("/image/classify", async (req, res) => {
//   const { url } = req.query;

//   if (!url || typeof url !== 'string') {
//     return res.status(400).send("Invalid or missing url parameter!");
//   }

//   return model.classify({
//     imageUrl: url,
//   }).then((predictions) => {
//     console.log(predictions);
//     return res.json(predictions);
//   }).catch((e) => {
//     console.error(e);
//     res.status(500).send("Something went wrong!")
//   });
// });

// app.listen(port, () => {
//   console.log(`Example app listening at http://localhost:${port}`);
// });