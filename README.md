# SailSnap

SailSnap is a lightweight SaaS platform designed for tourism companies to efficiently upload, manage, and deliver photos and videos to their customers. Built for speed, simplicity, and customization, it enables businesses such as parasailing tours, zipline operators, and adventure photographers to offer a streamlined media delivery and sales experience.

## Features

- Upload photos and videos from SD cards through a user-friendly dashboard
- Automatically generate watermarked previews
- Generate private, shareable gallery links for customers
- Provide secure download access after purchase
- Process payments using Stripe
- Customize branding (logos, colors) per business
- Automatically send customer emails with gallery access

## Tech Stack

### Frontend
- **React** (with **Vite** and **TypeScript**) – for building a fast, modern, and responsive user interface
- **Axios** – for communicating with the backend REST API

### Backend
- **Java with Spring Boot** – for REST API development and business logic
- **Spring Security** – for authenticated operator access and secure API endpoints
- **Stripe SDK** – for handling customer payments
- **AWS SDK** – for integrating with Amazon S3 and SES

### Database
- PostgreSQL (via Amazon RDS) – to store metadata such as galleries, purchases, and user data  

### File and Email Services
- Amazon S3 – for storing original and processed media files
- Amazon SES – for sending gallery links to customers via email
- AWS Lambda – for background processing tasks such as watermarking

### Hosting and DevOps
- **Docker** – containerization for local development and deployment
- **GitHub Actions** – for continuous integration and deployment (CI/CD)
- **Deployment Options**:
  - **Frontend**: Deploy using Vercel, Amazon S3 + CloudFront, or another static hosting solution
  - **Backend**: Deploy using AWS Elastic Beanstalk, EC2, or Fargate

## Getting Started

> Full setup instructions will be included as development progresses. This project is currently under active development.

## Project Objectives

1. Deliver a fast, professional media delivery system for small to medium-sized tourism companies.
2. Build a real-world SaaS product using scalable, production-ready technologies.
3. Demonstrate full-stack development and cloud integration skills aligned with large-scale software engineering roles.

## License

This project is licensed under a proprietary license. See the ['LICENSE'](./LICENSE.txt) file for details.
