# Moroccan Pizza App

A Moroccan-themed pizza ordering Android application with delivery tracking and payment options.

## Features

- **Moroccan-Themed Pizzas**: Discover unique pizzas with authentic Moroccan flavors and ingredients
- **Multiple Languages**: Interface available in both English and Arabic
- **Live Order Tracking**: Track your pizza delivery in real-time
- **Multiple Payment Options**: Pay with cash or credit card
- **User Profiles**: Save your delivery addresses and payment preferences
- **Customer Support**: In-app contact options for assistance

## App Structure

The app follows standard Android architecture patterns:

- **Activities**:
  - SplashActivity: Initial loading screen
  - MainActivity: Main container for all fragments

- **Fragments**:
  - HomeFragment: Main landing page with featured items
  - MenuFragment: Complete list of available pizzas
  - PizzaDetailFragment: Detailed view of selected pizza
  - CartFragment: Shopping cart management
  - CheckoutFragment: Order finalization and payment
  - OrderFragment: List of current and past orders
  - TrackingFragment: Real-time delivery tracking
  - ProfileFragment: User account management
  - SupportFragment: Customer assistance options

- **Database**:
  - Room Database for local storage
  - Remote API integration for backend communication

## Technical Details

- **Backend Communication**: Retrofit for API calls
- **Image Loading**: Glide for efficient image handling
- **Database**: Room for local persistence
- **UI Components**: Material Design components

## Development Status

This project is currently under development.