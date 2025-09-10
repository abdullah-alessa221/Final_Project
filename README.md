<img width="4832" height="530" alt="final drawio" src="https://github.com/user-attachments/assets/63c06de9-2dd2-4412-8db0-3131b7901745" /># Fawid
**Version:** 1.0  
**Developers:** Hatem Alawwad - ESSA ALGHANIM - ABDULLAH ALESSA

---

## APIs fetched:
- **Moyasar** (payments)  
- **WhatsApp Cloud API** (notifications)  
- **Adobe Acrobat PDF API** (documents)  
- **OpenAI via Spring AI** (LLM prompts: negotiation, summaries, translation)
- **VAPI** (AI agent calls)

---

## Overview
**Fawid** is a digital platform that streamlines buying and selling for **cars** and **real estate** in Saudi Arabia.  
It combines structured listings, **AI-assisted negotiation**, secure **payments**, and **document automation**—with multi-channel notifications—so deals close faster and with more trust.

---

## Goals
- Enable fair, fast negotiations powered by AI.  
- Provide transparent, documented transactions invoices.  
- Offer robust discovery and search for cars & properties.  
- Build user trust with verified payments and clear post-deal steps (ratings/reviews).

---

## Features
### Listings
- Create & manage **car** and **real estate** listings.  
- Saudi-friendly fields (fuel 91/95, Arabic city names, etc.).  
- Rich search with pagination & sorting.

### Negotiations & AI
- Buyer ↔ Seller negotiation threads with message history.  
- AI buyer-agent responses (concise Saudi tone).  
- Arabic summaries, bullet gists, and action items from transcripts.

### Payments & Documents
- Moyasar credit card flow with 3-D Secure and callbacks.  
- Auto-generate PDF **contracts/invoices** via Adobe API.  
- Store payment IDs/status and link to deals.

### Notifications
- WhatsApp messages for updates and receipts.  
- Email for OTP, summaries, and documents.  
- Call logs and (optional) call summaries.

### Subscriptions & Ratings
- Plans with feature flags (e.g., auto-negotiation).  
- Post-deal ratings & reviews.

---

##Tech

| Tech                  | Purpose                        |
| --------------------- | ------------------------------ |
| Java                  | Programming language           |
| Spring Boot           | Backend framework              |
| Spring Web            | Build RESTful APIs             |
| Spring Data JPA       | Database operations            |
| Spring Security       | Authentication & Authorization |
| Spring AI             | AI integration with Spring project |
| Jakarta Validation    | User input validation          |
| MySQL                 | Relational databases           |
| Lombok                | Reduce boilerplate code        |
| Maven                 | Build & dependency management  |
| JUnit                 | Unit testing                   |
| AWS                   | Cloud deployment               |
| GitHub                | Version control & collaboration |
| Moyasar               | Saudi payment gateway for secure transactions |
| Adobe Acrobat PDF API | Generate & export invoices     |
| OpenAI API            | LLM for negotiations, summaries & translations |
| VAPI                  | AI voice agent for automated phone calls |
| UltraMessage          | WhatsApp integration           |
| Postman               | API testing                    |
| Figma                 | UI/UX design                   |
| Linear                | Task management platform       |
---

## DP Diagram

<img width="735" height="684" alt="Screenshot 1447-03-18 at 7 05 35 AM" src="https://github.com/user-attachments/assets/ade08d7c-6f09-4e9c-a27f-5f0ec7fb08f9" />

## Usecase
<img width="4832" height="530" alt="final drawio" src="https://github.com/user-attachments/assets/d834d00d-5088-4d00-a6db-2a8cb67352b7" />

## Class Diagram
<img width="2672" height="1924" alt="Untitled_2" src="https://github.com/user-attachments/assets/c6c5ab10-0350-4250-a53d-c6b6a5402be8" />

# API Endpoints 


## Abdullah

## Buyer
- `updateBuyer` — Update buyer profile.  
- `requestOtp` — Request OTP for buyer registration.  
- `confirmOtp` — Confirm OTP to complete buyer registration.  
- `filterListings` — Filter/search listings for the buyer.  

## Seller
- `updateSeller` — Update seller profile.  
- `requestOtp` — Request OTP for seller registration.  
- `confirmOtp` — Confirm OTP for seller registration.  
- `getMyListingsByFilters` — Get the seller’s listings using filters.  
- `getMyNegotiationsStats` — Get summary stats for the seller’s negotiations.  

## User
- `getAllUsers` — List all users.  
- `getUserById` — Get a single user by ID.  
- `deleteUser` — Delete a user by ID.  
- `getAllUsersDTO` — List users (DTO projection).  
- `getAllSellersDTO` — List sellers (DTO projection).  
- `getAllBuyerDTO` — List buyers (DTO projection).  
- `blockUser` — Block a user.  
- `getBlockedUsers` — List blocked users.  
- `activeUser` — Activate/unblock a user.  

# Essa

## CarListing
- `getCarListings` — Get all car listings.  
- `listCar` — Create a new car listing.  

## RealEstateListing
- `getRealEstateListings` — Get all real-estate listings.  
- `listRealEstate` — Create a new real-estate listing.  

## Listing
- `getAllListings` — Get all listings (cars + real estate).  
- `getListingsBySeller` — Get listings for the current seller.  
- `getListingsByStatus` — Filter listings by status.  
- `getListingsByType` — Filter listings by type/subtype.  
- `searchListings` — Search listings by query.  
- `getListingById` — Get a listing by ID.  
- `deleteListing` — Delete a listing by ID.  

## Search (saved searches & results)
- `createCarSearch` — Save a car search for a buyer.  
- `createRealEstateSearch` — Save a real-estate search for a buyer.  
- `getSearchResults` — Get results for a saved search.   

## Payment
- `getAllPayments` — List all payment records.  
- `getPaymentById` — Get a payment record by DB ID.  
- `getPaymentStatus` — Check gateway status by payment ID.  
- `processPayment` — Create/process payment for a negotiation.  
- `processPaymentForSubscription` — Process payment for a subscription plan.  
- `handlePaymentCallback` — Handle gateway callback (status update).  

## CallLog
- `syncCallLogsFromApi` — Sync/import call logs for a seller from external provider.  
- `getAllCallLogs` — List call logs.  
- `getCallLogById` — Get a call log by internal ID.  
- `getCallLogsByPhoneNumber` — Filter by phone number.  
- `getCallLogsByStartedAt` — Filter by start timestamp.  
- `getCallLogsByStatus` — Filter by status.  
- `getCallLogsBySeller` — Filter by seller ID.  
- `getCallLogsByDateRange` — Filter by date range.  
- `getCallLogsBySellerAndStatus` — Filter by seller + status.  

## Rating
- `getAllByBuyer` — Ratings visible to the buyer.  
- `getBySeller` — Ratings visible to the seller.  
- `addRating` — Buyer rates a seller for a completed deal.  


## Hatem

## Negotiation
- `list` — List negotiations.  
- `get` — Get a negotiation by ID.  
- `createAi` — Start an AI-assisted negotiation for a listing.  
- `createManual` — Start a manual negotiation for a listing.  
- `propose` — Submit an offer/counter-offer.  
- `accept` — Accept a negotiation (lock agreed price).  
- `reject` — Reject/close a negotiation.  
- `enable` — Enable AI assistant for a negotiation.  
- `disable` — Disable AI assistant.  

## NegotiationMessage
- `listAll` — List all messages in a negotiation.  
- `sendByBuyer` — Buyer posts a message.  
- `sendBySeller` — Seller posts a message.  
- `summarize` — AI summary of a negotiation thread.



## Invoice
- `getAllInvoices` — List generated invoices/contracts.  
- `getInvoiceById` — Get invoice metadata by ID.  
- `downloadInvoice` — Download invoice PDF.  
- `generateInvoice` — Generate invoice/contract for a payment.  


## Subscription
- `getAll` — List subscription plans/user subscriptions.  
- `getSubscriptionById` — Get a subscription by ID.  
- `subscribeMonthly` — Start monthly subscription (after payment).  
- `subscribeYearly` — Start yearly subscription (after payment).  
- `cancelSubscription` — Cancel a subscription.  
- `setPhoneId` — Attach a device/phone ID to a subscription.  






