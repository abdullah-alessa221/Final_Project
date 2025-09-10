# Fawid
**Version:** 1.0  
**Developers:** Hatem Alawwad - ESSA ALGHANIM - ABDULLAH ALESSA

---

## APIs fetched:
- **Moyasar** (payments)  
- **WhatsApp Cloud API** (notifications)  
- **Adobe Acrobat PDF API** (documents)  
- **Fireflies** (AI meeting/transcript agent)  
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

## Tech Stack
- **Backend:** Spring Boot (Java 17)  
- **Database:** MySQL (AWS RDS)  
- **ORM:** Hibernate with JPA  
- **Deployment:** AWS (Elastic Beanstalk / ECS)  
- **CI/CD:** GitHub  
- **AI:** Spring AI (OpenAI compatible)

---

## DP Diagram

<img width="735" height="684" alt="Screenshot 1447-03-18 at 7 05 35 AM" src="https://github.com/user-attachments/assets/ade08d7c-6f09-4e9c-a27f-5f0ec7fb08f9" />





