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

Notes on gallery as this will be built iteratively:

Phase 1: Core Gallery MVP

Fields: id, businessId, name, createdAt, updatedAt, privateUrl, publicUrl, isPublic.
Backend functionality: create gallery, list galleries, get gallery.
Frontend: display gallery info and media, dynamically apply watermark if isPublic=false.
No tokenization, no email, no expiry yet.
Use mock S3 for testing media upload/download.

Phase 2: Optional Expiry & Public/Private Separation

Add shareToken (UUID) or reuse publicUrl for shareable links.
Add expiresAt if you want time-limited access.
Service: generate token or public URL, validate token/expiry on access.
Controller: endpoint for shared gallery access (GET /galleries/share/downloads?token=xxx).
This lets you separate public (unpaid/preview) vs private (paid/full) galleries.

Phase 3: URL Generation & Tokenization

Create secure, unique URLs for each gallery.
Ensure backend can verify token on access.
Frontend still uses these URLs to request media.

Phase 4: Email Service Integration

Service method: sendGalleryEmail(galleryId, email) → include public URL or share token.
Use SMTP / third-party service for real emails.
Frontend can trigger this via form.

FUTURE: 

Email integration (sendGalleryEmail()).

Optional URL tokenization or expiry for shared galleries.

Watermarked thumbnail generation (currently handled on frontend).

Logging, error handling, or audit trails for paid/unpaid state changes.

### Payment

TODO: Customer purchases, Stripe integration

FUTURE:

Email receipts to customers

Tokenized share links

Optional expiry for shared galleries

More detailed payment tracking/reporting

### Subscription

TODO: Business recurring SaaS payments, Stripe integration