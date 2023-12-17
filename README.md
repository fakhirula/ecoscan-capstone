


<!-- PROJECT LOGO -->
<div align="center">

#### Server Side Branch
## EcoScan : Organic and Inorganic Waste Detection

</div>
<br><hr><br>



<!-- ABOUT THE PROJECT -->
## About The Project


EcoScan is a mobile application developed to address the waste crisis in Indonesia. This application offers:
* Waste scanning
* Identification of waste types
* Recommendations
* Educational resources
<br>

The goal of this application is to empower individuals to make responsible waste management choices, with the hope of instilling a culture of environmental responsibility and contributing to a cleaner and healthier Indonesia.

<br>

## Built With

This section should list any major frameworks/libraries used to bootstrap your project. Leave any add-ons/plugins for the acknowledgements section. Here are a few examples.

<div style="text-align: center;">

![javascript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![express](https://img.shields.io/badge/Express.js-404D59?style=for-the-badge&logo=express)
![kotlin](https://img.shields.io/badge/Kotlin-007ACC?style=for-the-badge&logo=kotlin&logoColor=white)
![google cloud](https://img.shields.io/badge/Google_Cloud-4285F4?style=for-the-badge&logo=google-cloud&logoColor=white)

</div>

<br>


<!-- GETTING STARTED -->
# Getting Started
These instructions will help you set up and run the project on your local machine.

### Prerequisites
Make sure you have the following installed on your machine:

- [Node.js](https://nodejs.org/)
- [npm](https://www.npmjs.com/) (Node Package Manager)

<br><hr><br>

## Installation

_This is an example of how to list things you need to use the software and how to install them._

1. Clone the repo
   ```sh
   git clone -b server https://github.com/fakhirula/ecoscan-capstone.git
   ```
<br>

2. Install dependencies
   ```sh
   npm install
   ```
<br>

## Configuration

1. Copy the `.env.example` file to a new file named `.env`:

```bash
cp .env.example .env
```
<br>

2. Open the .env file and configure the variables according to your needs:
```bash
# PORT
PORT=

# JWT
JWT_SECRET=
JWT_EXPIRES_IN=

# STORAGE
PROJECT_ID=
BUCKET_NAME=

# DATABASE
HOST=
USER=
DATABASE=
PASSWORD=
```

<br>

## Usage

After completing the installation and configuration steps, you can start the application:

```bash
npm start
```
<br>

## API Documentation

- [x] Users
  - [x] Login
  - [x] Register
  - [x] Get all users
- [x] Image scan
  - [x] Get all scans
  - [x] Get scan by id
  - [x] Get scan by user
  - [x] Insert and predict scan
  - [x] Delete scan

<br>

See the [documentation](https://documenter.getpostman.com/view/23689594/2s9YkgEkY8) for a complete list of API features (and how to use them).

<br><hr><br>


<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

- Fork the Project
- Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
- Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
- Push to the Branch (`git push origin feature/AmazingFeature`)
- Open a Pull Request

<br>

<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<br>


<!-- CONTACT -->
## Contact

Fakhirul Akmal - [LinkedIn](https://www.linkedin.com/in/fakhirul-akmal/) - fakhirula27@gmail.com

Project Link: [https://github.com/fakhirula/ecoscan-capstone](https://github.com/fakhirula/ecoscan-capstone)

<br><br>