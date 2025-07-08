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
- React – for building a responsive, dynamic web interface
- Bootstrap – for fast, mobile-first UI styling
- Axios – for API communication between frontend and backend

### Backend
- Java with Spring Boot – REST API development with scalable architecture
- Spring Security (optional) – for authenticated operator access
- Stripe SDK – payment processing
- AWS SDK – integration with AWS services (S3, SES)

### Database
- PostgreSQL (via Amazon RDS) – to store metadata such as galleries, purchases, and user data  
  or  
- DynamoDB – for a serverless, managed NoSQL alternative

### File and Email Services
- Amazon S3 – for storing original and processed media files
- Amazon SES – for sending gallery links to customers via email
- AWS Lambda (optional) – for background processing tasks such as watermarking

### Hosting and DevOps
- React frontend hosted on Vercel, CloudFront, or S3 Static Website Hosting
- Spring Boot backend deployed using AWS Elastic Beanstalk, EC2, or Fargate
- Docker – containerization for development and deployment
- GitHub Actions – CI/CD pipeline for automated builds and deployments

## Getting Started

> Full setup instructions will be included as development progresses. This project is currently under active development.

## Project Objectives

1. Deliver a fast, professional media delivery system for small to medium-sized tourism companies.
2. Build a real-world SaaS product using scalable, production-ready technologies.
3. Demonstrate full-stack development and cloud integration skills aligned with large-scale software engineering roles.

## License

This project is licensed under a proprietary license. See the [LICENSE.txt](./LICENSE.txt)file for details.
