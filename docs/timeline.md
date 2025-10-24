# Project Timeline / TODO List

## 5 Domains for the app

Each domain has a:

- Entity - the table schema in the DB, defines how the data will actually look
- Repo - the interface to query / update that entity (data)
- Service - the business logic layer (fetch, validate, update, call Stripe, etc.)
- Controller - the REST API endpoints that the frontend calls, keeps layers thin by having this

### Business

Consists of accounts, branding, login / registration

### Media

Consists of uploaded photos / videos, metadata, S3 integration

### Gallery

NEXT: Groups media, generates sharable links, Stripe integration

### Payment

TODO: Customer purchases, Stripe integration

### Subscription

TODO: Business recurring SaaS payments, Stripe integration