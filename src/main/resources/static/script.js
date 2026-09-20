<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Raj Mart</title>

    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
            font-family: Arial, sans-serif;
        }

        body {
            background: #f5f7fb;
            color: #222;
        }

        /* =========================================
           HEADER
        ========================================= */

        .main-header {
            width: 100%;
            background: #ffffff;
            box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
            position: sticky;
            top: 0;
            z-index: 1000;
        }

        .header-top {
            max-width: 1400px;
            margin: auto;
            min-height: 76px;
            padding: 12px 25px;

            display: flex;
            align-items: center;
            gap: 30px;
        }

        /* LOGO */

        .brand {
            display: flex;
            align-items: center;
            gap: 9px;
            text-decoration: none;
            color: #111827;
            min-width: 170px;
        }

        .brand-icon {
            width: 45px;
            height: 45px;
            border-radius: 12px;

            display: flex;
            align-items: center;
            justify-content: center;

            font-size: 25px;

            background: linear-gradient(135deg, #2563eb, #7c3aed);
            box-shadow: 0 5px 15px rgba(37, 99, 235, 0.25);
        }

        .brand-name {
            font-size: 21px;
            font-weight: 900;
            letter-spacing: 1px;
        }

        .brand-sub {
            font-size: 11px;
            font-weight: 700;
            color: #2563eb;
            letter-spacing: 3px;
        }

        /* SEARCH */

        .header-search {
            flex: 1;
            max-width: 700px;
            height: 45px;

            display: flex;
            overflow: hidden;

            background: #f3f4f6;
            border: 1px solid #e5e7eb;
            border-radius: 8px;

            transition: 0.2s;
        }

        .header-search:focus-within {
            background: white;
            border-color: #2563eb;
            box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.1);
        }

        .header-search input {
            flex: 1;
            border: none;
            outline: none;
            background: transparent;

            padding: 0 16px;
            font-size: 14px;
        }

        .header-search button {
            width: 55px;
            border: none;
            cursor: pointer;

            background: #2563eb;
            color: white;

            font-size: 17px;
        }

        .header-search button:hover {
            background: #1d4ed8;
        }

        /* HEADER ACTIONS */

        .header-actions {
            display: flex;
            align-items: center;
            gap: 18px;
        }

        .header-action {
            text-decoration: none;
            color: #374151;

            display: flex;
            flex-direction: column;
            align-items: center;

            gap: 2px;
        }

        .header-action span {
            font-size: 21px;
        }

        .header-action small {
            font-size: 11px;
            font-weight: 700;
        }

        .header-action:hover {
            color: #2563eb;
        }

        .account-area {
            display: flex;
            align-items: center;
        }

        #userSection {
            display: inline-flex;
            align-items: center;
            gap: 10px;
        }

        .user-welcome {
            color: #2563eb;
            font-weight: bold;
            font-size: 13px;
        }

        .login-btn,
        .logout-btn {
            border: none;
            padding: 8px 14px;
            border-radius: 5px;
            cursor: pointer;
            font-weight: bold;
        }

        .login-btn {
            background: #2563eb;
            color: white;
        }

        .logout-btn {
            background: #ef4444;
            color: white;
        }

        .login-btn:hover {
            background: #1d4ed8;
        }

        .logout-btn:hover {
            background: #dc2626;
        }

        /* CATEGORY NAV */

        .category-nav {
            border-top: 1px solid #f0f0f0;
            background: #ffffff;
        }

        .category-inner {
            max-width: 1400px;
            margin: auto;

            min-height: 43px;
            padding: 0 25px;

            display: flex;
            align-items: center;
            gap: 32px;
        }

        .category-inner a {
            text-decoration: none;
            color: #374151;

            font-size: 13px;
            font-weight: 700;

            transition: 0.2s;
        }

        .category-inner a:hover {
            color: #2563eb;
        }

        /* =========================================
           HERO
        ========================================= */

        .hero-section {
            max-width: 1400px;
            min-height: 470px;

            margin: 25px auto;
            padding: 55px 75px;

            border-radius: 24px;

            position: relative;
            overflow: hidden;

            display: flex;
            align-items: center;

            background:
                radial-gradient(
                    circle at 80% 20%,
                    rgba(124, 58, 237, 0.35),
                    transparent 30%
                ),
                radial-gradient(
                    circle at 20% 90%,
                    rgba(37, 99, 235, 0.3),
                    transparent 35%
                ),
                linear-gradient(
                    135deg,
                    #0f172a,
                    #172554 55%,
                    #312e81
                );

            color: white;
        }

        .hero-content {
            width: 55%;
            position: relative;
            z-index: 3;
        }

        .hero-badge {
            display: inline-block;

            padding: 8px 15px;
            margin-bottom: 18px;

            border-radius: 30px;

            background: rgba(255, 255, 255, 0.1);
            border: 1px solid rgba(255, 255, 255, 0.2);

            font-size: 12px;
            font-weight: 800;
            letter-spacing: 1px;
        }

        .hero-content h1 {
            margin: 0;

            font-size: clamp(40px, 5vw, 68px);
            line-height: 1.05;

            font-weight: 900;
        }

        .hero-content h1 span {
            display: block;

            background: linear-gradient(
                90deg,
                #60a5fa,
                #c084fc
            );

            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }

        .hero-content p {
            max-width: 580px;

            margin: 22px 0;

            color: #cbd5e1;

            font-size: 16px;
            line-height: 1.7;
        }

        .hero-buttons {
            display: flex;
            gap: 12px;
        }

        .hero-shop-btn,
        .hero-explore-btn {
            padding: 13px 23px;

            border-radius: 8px;

            font-size: 14px;
            font-weight: 800;

            cursor: pointer;
            transition: 0.2s;
        }

        .hero-shop-btn {
            border: none;

            background: white;
            color: #172554;
        }

        .hero-shop-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.25);
        }

        .hero-explore-btn {
            border: 1px solid rgba(255, 255, 255, 0.3);

            background: rgba(255, 255, 255, 0.08);
            color: white;
        }

        .hero-explore-btn:hover {
            background: rgba(255, 255, 255, 0.15);
        }

        .hero-features {
            display: flex;
            gap: 25px;
            margin-top: 35px;
        }

        .hero-features div {
            display: flex;
            align-items: center;
            gap: 8px;

            color: #dbeafe;
            font-size: 12px;
            font-weight: 700;
        }

        .hero-features strong {
            font-size: 18px;
        }

        /* HERO VISUAL */

        .hero-visual {
            position: absolute;

            right: 45px;
            top: 0;

            width: 45%;
            height: 100%;
        }

        .hero-circle {
            position: absolute;

            width: 340px;
            height: 340px;

            border-radius: 50%;

            right: 70px;
            top: 65px;

            background: rgba(255, 255, 255, 0.06);
            border: 1px solid rgba(255, 255, 255, 0.1);

            box-shadow:
                0 0 80px rgba(96, 165, 250, 0.15);
        }

        .hero-product {
            position: absolute;

            right: 130px;
            top: 110px;

            width: 230px;
            height: 230px;

            border-radius: 40px;

            display: flex;
            align-items: center;
            justify-content: center;

            font-size: 115px;

            background: rgba(255, 255, 255, 0.08);
            backdrop-filter: blur(10px);

            border: 1px solid rgba(255, 255, 255, 0.15);

            transform: rotate(-8deg);

            box-shadow:
                0 25px 60px rgba(0, 0, 0, 0.25);
        }

        .floating-card {
            position: absolute;

            padding: 12px 17px;

            display: flex;
            align-items: center;
            gap: 8px;

            border-radius: 12px;

            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(12px);

            border: 1px solid rgba(255, 255, 255, 0.15);

            font-size: 13px;
            font-weight: 800;

            z-index: 4;

            animation: floating 3s ease-in-out infinite;
        }

        .card-one {
            right: 25px;
            top: 105px;
        }

        .card-two {
            left: 10px;
            bottom: 110px;

            animation-delay: 1s;
        }

        .hero-product-label {
            position: absolute;

            right: 100px;
            bottom: 55px;

            display: flex;
            flex-direction: column;

            text-align: center;

            z-index: 5;
        }

        .hero-product-label strong {
            font-size: 17px;
        }

        .hero-product-label span {
            color: #cbd5e1;
            font-size: 11px;
            margin-top: 3px;
        }

        @keyframes floating {
            0%, 100% {
                transform: translateY(0);
            }

            50% {
                transform: translateY(-8px);
            }
        }

        /* =========================================
           CATEGORIES
        ========================================= */

        .categories-section {
            max-width: 1400px;
            margin: 35px auto;
            padding: 0 25px;
        }

        .section-heading {
            display: flex;
            align-items: flex-end;
            justify-content: space-between;
            margin-bottom: 20px;
        }

        .section-tag {
            display: block;
            color: #2563eb;
            font-size: 11px;
            font-weight: 900;
            letter-spacing: 2px;
            margin-bottom: 5px;
        }

        .section-heading h2 {
            margin: 0;
            color: #111827;
            font-size: 28px;
            font-weight: 900;
        }

        .view-all {
            text-decoration: none;
            color: #2563eb;
            font-size: 13px;
            font-weight: 800;
        }

        .view-all:hover {
            text-decoration: underline;
        }

        .category-cards {
            display: grid;
            grid-template-columns: repeat(6, 1fr);
            gap: 15px;
        }

        .category-card {
            min-height: 150px;
            padding: 20px 15px;

            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;

            text-align: center;
            text-decoration: none;

            background: #ffffff;
            border: 1px solid #e5e7eb;
            border-radius: 16px;

            box-shadow:
                0 3px 12px rgba(0, 0, 0, 0.04);

            transition: all 0.25s ease;
        }

        .category-card:hover {
            transform: translateY(-6px);
            border-color: #bfdbfe;
            box-shadow:
                0 12px 25px rgba(37, 99, 235, 0.12);
        }

        .category-icon {
            width: 65px;
            height: 65px;

            margin-bottom: 12px;

            display: flex;
            align-items: center;
            justify-content: center;

            border-radius: 18px;

            font-size: 31px;
        }

        .gaming-icon {
            background: #ede9fe;
        }

        .keyboard-icon {
            background: #dbeafe;
        }

        .audio-icon {
            background: #fce7f3;
        }

        .mouse-icon {
            background: #dcfce7;
        }

        .tech-icon {
            background: #fef3c7;
        }

        .deals-icon {
            background: #fee2e2;
        }

        .category-card h3 {
            margin: 0;
            color: #111827;
            font-size: 15px;
            font-weight: 800;
        }

        .category-card p {
            margin: 5px 0 0;
            color: #6b7280;
            font-size: 11px;
        }

        /* =========================================
           PRODUCTS
        ========================================= */

        .products-section {
            max-width: 1400px;
            margin: 45px auto;
            padding: 0 25px;
        }

        .products-heading {
            display: flex;
            align-items: flex-end;
            justify-content: space-between;
            margin-bottom: 22px;
        }

        .products-heading h2 {
            margin: 0;
            color: #111827;
            font-size: 30px;
            font-weight: 900;
        }

        .products-heading p {
            margin: 6px 0 0;
            color: #6b7280;
            font-size: 13px;
        }

        /* SEARCH FILTER */

        .product-controls {
            display: flex;
            gap: 12px;
            margin-bottom: 25px;
        }

        .product-controls input,
        .product-controls select {
            height: 44px;
            border: 1px solid #e5e7eb;
            border-radius: 8px;
            padding: 0 14px;
            outline: none;
            background: white;
        }

        .product-controls input {
            flex: 1;
        }

        .product-controls input:focus,
        .product-controls select:focus {
            border-color: #2563eb;
            box-shadow:
                0 0 0 3px rgba(37, 99, 235, 0.08);
        }

        /* PRODUCT GRID */

        #productList {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 18px;
        }

        /* PRODUCT CARD */

        .product-card {
            position: relative;
            overflow: hidden;

            background: #ffffff;
            border: 1px solid #e5e7eb;
            border-radius: 16px;

            padding: 18px;

            box-shadow:
                0 4px 15px rgba(0, 0, 0, 0.04);

            transition:
                transform 0.25s ease,
                box-shadow 0.25s ease,
                border-color 0.25s ease;
        }

        .product-card:hover {
            transform: translateY(-5px);
            border-color: #bfdbfe;
            box-shadow:
                0 15px 35px rgba(0, 0, 0, 0.10);
        }

        .product-image {
            width: 100%;
            height: 190px;

            display: flex;
            align-items: center;
            justify-content: center;

            margin-bottom: 16px;

            border-radius: 12px;

            background:
                linear-gradient(
                    135deg,
                    #f8fafc,
                    #eef2ff
                );

            font-size: 75px;
            position: relative;
        }

        .product-icon {
            font-size: 75px;
        }

        .product-card h3 {
            margin: 0 0 8px;

            color: #111827;

            font-size: 16px;
            font-weight: 800;

            line-height: 1.4;
        }

        .product-price {
            margin: 8px 0;

            color: #111827;
            font-size: 24px;
            font-weight: 900;
        }

        .product-stock {
            margin: 8px 0;
            font-size: 12px;
            font-weight: 700;
        }

        .stock-available {
            color: #16a34a;
        }

        .stock-low {
            color: #ea580c;
        }

        .stock-out {
            color: #dc2626;
        }

        /* RATING */

        .product-rating {
            margin: 10px 0;
            padding: 10px;

            border-radius: 8px;

            background: #f8fafc;
            border: 1px solid #eef2f7;
        }

        .rating-stars {
            display: flex;
            justify-content: center;
            gap: 3px;
            margin-bottom: 5px;
        }

        .rating-star {
            border: none !important;
            background: transparent !important;
            color: #d1d5db !important;
            padding: 0 !important;

            font-size: 22px;
            line-height: 1;

            cursor: pointer !important;
            box-shadow: none !important;
        }

        .rating-star:hover {
            background: transparent !important;
            color: #ffc107 !important;
            transform: scale(1.08);
        }

        .rating-star.filled {
            color: #ffc107 !important;
        }

        .rating-info {
            text-align: center;
            font-size: 12px;
            color: #555;
        }

        /* REVIEWS */

        .reviews-box {
            margin-top: 10px;
            max-height: 170px;
            overflow-y: auto;
        }

        .no-reviews {
            font-size: 12px;
            color: #888;
            padding: 6px 0;
            text-align: center;
        }

        .customer-review {
            background: #ffffff;
            border: 1px solid #e0e0e0;
            border-radius: 7px;
            padding: 9px;
            margin-top: 8px;
        }

        .review-stars {
            color: #ffc107;
            font-size: 15px;
            margin-bottom: 4px;
        }

        .review-author {
            font-size: 12px;
            margin-bottom: 4px;
        }

        .review-text {
            font-size: 12px;
            color: #333;
            line-height: 1.4;
            word-break: break-word;
        }

        .review-date {
            margin-top: 5px;
            font-size: 10px;
            color: #999;
        }

        /* REVIEW BUTTON */

        .review-btn {
            width: 100%;
            margin-top: 8px;

            border: none;
            border-radius: 7px;

            padding: 9px;

            background: #008844;
            color: white;

            font-size: 12px;
            font-weight: bold;

            cursor: pointer;
        }

        .review-btn:hover {
            background: #006b36;
        }

        /* REVIEW FORM */

        .review-form {
            display: none;
            margin-top: 10px;
            padding: 10px;

            background: #f8fafc;
            border-radius: 8px;
        }

        .review-form textarea {
            width: 100%;
            min-height: 75px;
            resize: vertical;

            padding: 8px;

            border: 1px solid #ccc;
            border-radius: 6px;

            font-family: Arial, sans-serif;
            box-sizing: border-box;
        }

        .review-form textarea:focus {
            outline: 2px solid #00aa66;
        }

        .submit-review {
            width: 100%;
            margin-top: 8px;

            border: none;
            border-radius: 6px;

            background: #111;
            color: white;

            padding: 9px;

            font-size: 12px;
            font-weight: bold;

            cursor: pointer;
        }

        .submit-review:hover {
            background: #00aa66;
        }

        /* PRODUCT BUTTONS */

        .product-buttons {
            display: flex;
            flex-direction: column;
            gap: 8px;
            margin-top: 14px;
        }

        .cart-buy-row {
            display: flex;
            gap: 8px;
        }

        .add-cart-btn,
        .buy-now-btn {
            flex: 1;

            min-height: 42px;

            border-radius: 8px;

            font-size: 12px;
            font-weight: 800;

            cursor: pointer;

            transition: 0.2s;
        }

        .add-cart-btn {
            border: 1px solid #2563eb;

            background: white;
            color: #2563eb;
        }

        .add-cart-btn:hover {
            background: #eff6ff;
        }

        .buy-now-btn {
            border: none;

            background: #2563eb;
            color: white;
        }

        .buy-now-btn:hover {
            background: #1d4ed8;
        }

        .product-card button:disabled {
            cursor: not-allowed;
            opacity: 0.55;
        }

        /* VIEW DETAILS */

        .view-details-btn {
            display: block;

            width: 100%;

            padding: 11px;

            text-align: center;
            text-decoration: none;

            border: 1px solid #2874f0;
            border-radius: 8px;

            background: white;
            color: #2874f0;

            font-weight: bold;
            font-size: 12px;

            box-sizing: border-box;

            transition: 0.2s;
        }

        .view-details-btn:hover {
            background: #2874f0;
            color: white;
        }

        /* DEAL BADGE */

        .product-badge {
            position: absolute;

            top: 10px;
            left: 10px;

            padding: 5px 9px;

            border-radius: 5px;

            background: #dc2626;
            color: white;

            font-size: 10px;
            font-weight: 900;

            z-index: 2;
        }

        /* MESSAGE */

        #message {
            text-align: center;
            margin: 20px;
            font-weight: bold;
            color: #555;
        }

        /* =========================================
           CHATBOT
        ========================================= */

        #chatbotButton {
            position: fixed;

            right: 25px;
            bottom: 25px;

            width: 60px;
            height: 60px;

            border: none;
            border-radius: 50%;

            background: #2563eb;
            color: white;

            font-size: 28px;

            cursor: pointer;

            box-shadow:
                0 4px 15px rgba(0, 0, 0, 0.3);

            z-index: 9999;
        }

        #chatbotButton:hover {
            transform: scale(1.08);
        }

        #chatbotBox {
            display: none;

            position: fixed;

            right: 25px;
            bottom: 95px;

            width: 350px;
            height: 480px;

            background: white;

            border-radius: 15px;

            box-shadow:
                0 5px 25px rgba(0, 0, 0, 0.3);

            overflow: hidden;

            z-index: 9999;
        }

        #chatbotHeader {
            background: #111;

            color: #60a5fa;

            padding: 15px;

            font-weight: bold;

            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        #chatMessages {
            height: 360px;

            padding: 15px;

            overflow-y: auto;

            background: #f5f5f5;
        }

        .chat-message {
            margin-bottom: 12px;

            padding: 10px;

            border-radius: 10px;

            max-width: 85%;

            white-space: pre-line;
        }

        .bot-message {
            background: #e5e5e5;
            color: #111;
        }

        .user-message {
            background: #2563eb;
            color: white;

            margin-left: auto;
        }

        #chatInputArea {
            display: flex;

            border-top: 1px solid #ddd;
        }

        #chatInput {
            flex: 1;

            padding: 12px;

            border: none;

            outline: none;
        }

        #chatSend {
            border: none;

            background: #111;
            color: white;

            padding: 0 18px;

            cursor: pointer;
        }

        #chatSend:hover {
            background: #2563eb;
        }

        /* =========================================
           FOOTER
        ========================================= */

        footer {
            margin-top: 40px;

            background: #111827;
            color: white;

            text-align: center;

            padding: 25px;
        }

        /* =========================================
           RESPONSIVE
        ========================================= */

        @media (max-width: 1100px) {

            #productList {
                grid-template-columns: repeat(3, 1fr);
            }

            .category-cards {
                grid-template-columns: repeat(3, 1fr);
            }
        }

        @media (max-width: 900px) {

            .header-top {
                padding: 12px 15px;
                gap: 12px;
            }

            .brand {
                min-width: auto;
            }

            .brand-name {
                font-size: 17px;
            }

            .brand-sub {
                font-size: 9px;
            }

            .header-actions {
                gap: 10px;
            }

            .header-action small {
                display: none;
            }

            .category-inner {
                overflow-x: auto;
                gap: 22px;
                padding: 0 15px;
                white-space: nowrap;
            }

            .hero-section {
                margin: 15px;
                padding: 40px 25px;
                min-height: 520px;
            }

            .hero-content {
                width: 100%;
            }

            .hero-content h1 {
                font-size: 42px;
            }

            .hero-visual {
                opacity: 0.35;
                width: 100%;
                right: 0;
            }

            .hero-features {
                flex-wrap: wrap;
                gap: 12px;
            }

            #productList {
                grid-template-columns: repeat(2, 1fr);
            }
        }

        @media (max-width: 600px) {

            .header-top {
                flex-wrap: wrap;
            }

            .brand {
                order: 1;
            }

            .header-actions {
                order: 2;
                margin-left: auto;
            }

            .header-search {
                order: 3;
                flex-basis: 100%;
                max-width: none;
            }

            .hero-section {
                min-height: 560px;
                border-radius: 18px;
            }

            .hero-content h1 {
                font-size: 38px;
            }

            .hero-content p {
                font-size: 14px;
            }

            .hero-buttons {
                flex-direction: column;
                align-items: stretch;
            }

            .hero-shop-btn,
            .hero-explore-btn {
                width: 100%;
            }

            .hero-product {
                right: 50px;

                width: 180px;
                height: 180px;

                font-size: 80px;
            }

            .hero-circle {
                right: 20px;
            }

            .categories-section {
                padding: 0 15px;
                margin: 25px auto;
            }

            .section-heading h2 {
                font-size: 22px;
            }

            .category-cards {
                grid-template-columns: repeat(2, 1fr);
                gap: 10px;
            }

            .category-card {
                min-height: 135px;
            }

            .products-section {
                padding: 0 15px;
            }

            .products-heading h2 {
                font-size: 24px;
            }

            .product-controls {
                flex-direction: column;
            }

            .product-controls input,
            .product-controls select {
                width: 100%;
            }

            #productList {
                grid-template-columns: 1fr;
            }

            .product-image {
                height: 210px;
            }

            #chatbotBox {
                right: 10px;
                bottom: 85px;
                width: calc(100% - 20px);
                height: 450px;
            }

            #chatMessages {
                height: 330px;
            }

            #chatbotButton {
                right: 15px;
                bottom: 15px;
            }

            #userSection {
                flex-wrap: wrap;
                justify-content: center;
            }
        }
    </style>
</head>

<body>

    <!-- =========================================
         HEADER
    ========================================= -->

    <header class="main-header">

        <div class="header-top">

            <!-- LOGO -->

            <a href="index.html" class="brand">

                <div class="brand-icon">
                    🛒
                </div>

                <div>
                    <div class="brand-name">
                        RAJ
                    </div>

                    <div class="brand-sub">
                        MART
                    </div>
                </div>

            </a>

            <!-- SEARCH -->

            <div class="header-search">

                <input
                    id="headerSearchInput"
                    type="text"
                    placeholder="Search for products, brands and more..."
                    oninput="syncSearchAndFilter()"
                >

                <button
                    type="button"
                    onclick="searchFromHeader()">
                    🔍
                </button>

            </div>

            <!-- HEADER ACTIONS -->

            <div class="header-actions">

                <a
                    href="orders.html"
                    class="header-action">

                    <span>📦</span>
                    <small>Orders</small>

                </a>

                <a
                    href="cart.html"
                    class="header-action cart-action">

                    <span>🛒</span>
                    <small>Cart</small>

                </a>

                <div
                    id="userSection"
                    class="account-area">
                </div>

            </div>

        </div>

        <!-- CATEGORY NAV -->

        <div class="category-nav">

            <div class="category-inner">

                <a href="#products">
                    🏠 Home
                </a>

                <a
                    href="#products"
                    onclick="filterByCategory('gaming')">
                    🎮 Gaming
                </a>

                <a
                    href="#products"
                    onclick="filterByCategory('keyboard')">
                    ⌨️ Accessories
                </a>

                <a
                    href="#products"
                    onclick="filterByCategory('electronics')">
                    🖥️ Electronics
                </a>

                <a
                    href="#products"
                    onclick="showDeals()">
                    🔥 Deals
                </a>

                <a
                    href="#products"
                    onclick="showTopRated()">
                    ⭐ Top Rated
                </a>

            </div>

        </div>

    </header>


    <!-- =========================================
         HERO
    ========================================= -->

    <section class="hero-section">

        <div class="hero-content">

            <div class="hero-badge">
                🔥 TRENDING PRODUCTS
            </div>

            <h1>
                Upgrade Your
                <span>Gaming Setup</span>
            </h1>

            <p>
                Discover premium gaming accessories,
                electronics and everyday tech at amazing
                prices.
            </p>

            <div class="hero-buttons">

                <button
                    type="button"
                    class="hero-shop-btn"
                    onclick="scrollToProducts()">
                    Shop Now →
                </button>

                <button
                    type="button"
                    class="hero-explore-btn"
                    onclick="scrollToProducts()">
                    Explore Products
                </button>

            </div>

            <div class="hero-features">

                <div>
                    <strong>⚡</strong>
                    <span>Fast Delivery</span>
                </div>

                <div>
                    <strong>🛡️</strong>
                    <span>Secure Shopping</span>
                </div>

                <div>
                    <strong>💯</strong>
                    <span>Quality Products</span>
                </div>

            </div>

        </div>

        <div class="hero-visual">

            <div class="hero-circle"></div>

            <div class="floating-card card-one">
                🎮
                <span>Gaming</span>
            </div>

            <div class="floating-card card-two">
                ⌨️
                <span>Accessories</span>
            </div>

            <div class="hero-product">
                🎧
            </div>

            <div class="hero-product-label">

                <strong>
                    Premium Tech
                </strong>

                <span>
                    Built for your setup
                </span>

            </div>

        </div>

    </section>


    <!-- =========================================
         CATEGORIES
    ========================================= -->

    <section class="categories-section">

        <div class="section-heading">

            <div>

                <span class="section-tag">
                    EXPLORE
                </span>

                <h2>
                    Shop by Category
                </h2>

            </div>

            <a
                href="#products"
                class="view-all">
                View All →
            </a>

        </div>

        <div class="category-cards">

            <a
                href="#products"
                class="category-card"
                onclick="filterByCategory('gaming')">

                <div class="category-icon gaming-icon">
                    🎮
                </div>

                <h3>
                    Gaming
                </h3>

                <p>
                    Level up your setup
                </p>

            </a>


            <a
                href="#products"
                class="category-card"
                onclick="filterByCategory('keyboard')">

                <div class="category-icon keyboard-icon">
                    ⌨️
                </div>

                <h3>
                    Keyboards
                </h3>

                <p>
                    Fast & responsive
                </p>

            </a>


            <a
                href="#products"
                class="category-card"
                onclick="filterByCategory('audio')">

                <div class="category-icon audio-icon">
                    🎧
                </div>

                <h3>
                    Audio
                </h3>

                <p>
                    Immersive sound
                </p>

            </a>


            <a
                href="#products"
                class="category-card"
                onclick="filterByCategory('accessories')">

                <div class="category-icon mouse-icon">
                    🖱️
                </div>

                <h3>
                    Accessories
                </h3>

                <p>
                    Complete your setup
                </p>

            </a>


            <a
                href="#products"
                class="category-card"
                onclick="filterByCategory('electronics')">

                <div class="category-icon tech-icon">
                    💻
                </div>

                <h3>
                    Electronics
                </h3>

                <p>
                    Latest technology
                </p>

            </a>


            <a
                href="#products"
                class="category-card"
                onclick="showDeals()">

                <div class="category-icon deals-icon">
                    🔥
                </div>

                <h3>
                    Best Deals
                </h3>

                <p>
                    Save more today
                </p>

            </a>

        </div>

    </section>


    <!-- =========================================
         PRODUCTS
    ========================================= -->

    <section
        class="products-section"
        id="products">

        <div class="products-heading">

            <div>

                <span class="section-tag">
                    RAJ MART
                </span>

                <h2>
                    Available Products
                </h2>

                <p>
                    Choose your favourite products
                    and upgrade your setup.
                </p>

            </div>

        </div>


        <!-- SEARCH + FILTER -->

        <div class="product-controls">

            <input
                type="text"
                id="searchInput"
                placeholder="🔍 Search products..."
                oninput="filterProducts()"
            >

            <select
                id="stockFilter"
                onchange="filterProducts()">

                <option value="all">
                    All Products
                </option>

                <option value="available">
                    In Stock
                </option>

                <option value="out">
                    Out of Stock
                </option>

            </select>

        </div>


        <!-- MESSAGE -->

        <div id="message">
            Loading products...
        </div>


        <!-- PRODUCT LIST -->

        <div id="productList"></div>

    </section>


    <!-- =========================================
         AI CHATBOT
    ========================================= -->

    <button
        id="chatbotButton"
        type="button"
        onclick="toggleChatbot()">
        🤖
    </button>


    <div id="chatbotBox">

        <div id="chatbotHeader">

            <span>
                🤖 Raj AI Assistant
            </span>

            <span
                onclick="toggleChatbot()"
                style="cursor:pointer;">
                ✕
            </span>

        </div>


        <div id="chatMessages">

            <div class="chat-message bot-message">

                👋 Hello! I'm Raj AI Assistant.

                I can help you with:

                • Products
                • Prices
                • Stock
                • Cart
                • Orders

                Try asking:

                "What products are available?"

            </div>

        </div>


        <div id="chatInputArea">

            <input
                type="text"
                id="chatInput"
                placeholder="Ask something..."
                onkeydown="handleChatKey(event)"
            >

            <button
                id="chatSend"
                type="button"
                onclick="sendChatMessage()">
                Send
            </button>

        </div>

    </div>


    <!-- =========================================
         FOOTER
    ========================================= -->

    <footer>

        © 2026 Raj Mart.
        All Rights Reserved.

    </footer>


    <!-- =========================================
         JAVASCRIPT
    ========================================= -->

    <script>

        /* =========================================
           GLOBAL PRODUCTS
        ========================================= */

        let allProducts = [];


        /* =========================================
           USER LOGIN / LOGOUT
        ========================================= */

        function updateUserSection() {

            const userSection =
                document.getElementById("userSection");

            if (!userSection) {
                return;
            }

            const loggedIn =
                localStorage.getItem("loggedIn");

            const email =
                localStorage.getItem("userEmail");

            const role =
                localStorage.getItem("userRole") ||
                localStorage.getItem("role");

            if (
                loggedIn === "true" &&
                email
            ) {

                let displayName =
                    localStorage.getItem("userName");

                if (!displayName) {
                    displayName =
                        email.split("@")[0];
                }

                userSection.innerHTML = `

                    <span class="user-welcome">
                        👤 Welcome, ${escapeHtml(displayName)}
                    </span>

                    <button
                        type="button"
                        class="logout-btn"
                        onclick="logoutUser()">

                        Logout 🚪

                    </button>
                `;

            } else {

                userSection.innerHTML = `

                    <button
                        type="button"
                        class="login-btn"
                        onclick="window.location.href='login.html'">

                        Login 🔐

                    </button>

                `;

            }

        }


        /* =========================================
           LOGOUT
        ========================================= */

        function logoutUser() {

            const confirmLogout =
                confirm(
                    "Are you sure you want to logout?"
                );

            if (!confirmLogout) {
                return;
            }

            localStorage.removeItem("loggedIn");
            localStorage.removeItem("userEmail");
            localStorage.removeItem("userRole");
            localStorage.removeItem("role");
            localStorage.removeItem("userName");

            alert(
                "You have been logged out successfully."
            );

            window.location.href =
                "login.html";
        }


        /* =========================================
           LOAD PRODUCTS
        ========================================= */

        async function loadProducts() {

            const message =
                document.getElementById("message");

            try {

                message.innerText =
                    "Loading products...";

                const response =
                    await fetch("/api/products");

                if (!response.ok) {

                    throw new Error(
                        "Failed to load products"
                    );

                }

                allProducts =
                    await response.json();

                filterProducts();

            } catch (error) {

                console.error(
                    "Product loading error:",
                    error
                );

                message.innerText =
                    "Unable to load products. Please check the server.";

            }

        }


        /* =========================================
           SEARCH FROM HEADER
        ========================================= */

        function syncSearchAndFilter() {

            const headerSearch =
                document.getElementById(
                    "headerSearchInput"
                );

            const searchInput =
                document.getElementById(
                    "searchInput"
                );

            if (
                headerSearch &&
                searchInput
            ) {

                searchInput.value =
                    headerSearch.value;

                filterProducts();

            }

        }


        function searchFromHeader() {

            const headerSearch =
                document.getElementById(
                    "headerSearchInput"
                );

            const searchInput =
                document.getElementById(
                    "searchInput"
                );

            if (
                headerSearch &&
                searchInput
            ) {

                searchInput.value =
                    headerSearch.value;

                filterProducts();

                scrollToProducts();

            }

        }


        /* =========================================
           FILTER PRODUCTS
        ========================================= */

        function filterProducts() {

            const searchInput =
                document.getElementById(
                    "searchInput"
                );

            const stockFilter =
                document.getElementById(
                    "stockFilter"
                );

            const productList =
                document.getElementById(
                    "productList"
                );

            const message =
                document.getElementById(
                    "message"
                );

            if (
                !searchInput ||
                !stockFilter ||
                !productList
            ) {
                return;
            }

            const searchText =
                searchInput.value
                    .toLowerCase()
                    .trim();

            const selectedStock =
                stockFilter.value;


            /* FILTER */

            const filteredProducts =
                allProducts.filter(product => {

                    const productName =
                        String(
                            product.name || ""
                        ).toLowerCase();

                    const matchesSearch =
                        productName.includes(
                            searchText
                        );

                    let matchesStock =
                        true;

                    if (
                        selectedStock ===
                        "available"
                    ) {

                        matchesStock =
                            Number(
                                product.quantity
                            ) > 0;

                    }

                    if (
                        selectedStock ===
                        "out"
                    ) {

                        matchesStock =
                            Number(
                                product.quantity
                            ) <= 0;

                    }

                    return (
                        matchesSearch &&
                        matchesStock
                    );

                });


            productList.innerHTML = "";


            if (
                filteredProducts.length === 0
            ) {

                message.innerText =
                    "No matching products found.";

                return;

            }


            message.innerText = "";


            /* DISPLAY */

            filteredProducts.forEach(product => {

                const card =
                    document.createElement("div");

                card.className =
                    "product-card";


                const quantity =
                    Number(product.quantity);

                const outOfStock =
                    quantity <= 0;


                /* STOCK */

                let stockHTML = "";

                if (outOfStock) {

                    stockHTML = `

                        <div
                            class="product-stock stock-out">

                            ❌ Out of Stock

                        </div>

                    `;

                } else if (quantity <= 5) {

                    stockHTML = `

                        <div
                            class="product-stock stock-low">

                            ⚠️ Only ${quantity} left

                        </div>

                    `;

                } else {

                    stockHTML = `

                        <div
                            class="product-stock stock-available">

                            ✓ In Stock (${quantity})

                        </div>

                    `;

                }


                /* DEAL */

                let dealBadge = "";

                if (!outOfStock) {

                    dealBadge = `

                        <div class="product-badge">
                            🔥 DEAL
                        </div>

                    `;

                }


                /* PRODUCT CARD */

                card.innerHTML = `

                    <div class="product-image">

                        ${dealBadge}

                        <div class="product-icon">
                            ${getProductIcon(product.name)}
                        </div>

                    </div>


                    <div class="product-info">

                        <h3>
                            ${escapeHtml(product.name)}
                        </h3>


                        <div class="product-price">
                            ₹${Number(product.price).toFixed(2)}
                        </div>


                        ${stockHTML}


                        <!-- RATING -->

                        <div class="product-rating">

                            <div
                                class="rating-stars"
                                id="stars-${product.id}">

                                ${getRatingStars(product.id)}

                            </div>


                            <div
                                class="rating-info"
                                id="rating-info-${product.id}">

                                Loading rating...

                            </div>

                        </div>


                        <!-- REVIEWS -->

                        <div
                            id="reviews-${product.id}"
                            class="reviews-box">

                            Loading reviews...

                        </div>


                        <!-- WRITE REVIEW -->

                        <button
                            type="button"
                            class="review-btn"
                            onclick="toggleReviewForm(${product.id})">

                            ✍️ Write a Review

                        </button>


                        <!-- REVIEW FORM -->

                        <div
                            id="review-form-${product.id}"
                            class="review-form">

                            <div
                                style="text-align:center; margin-bottom:8px;">

                                Select your rating above ⭐

                            </div>

                            <textarea
                                id="review-text-${product.id}"
                                placeholder="Write your review..."
                                maxlength="300">
                            </textarea>


                            <button
                                type="button"
                                class="submit-review"
                                onclick="submitReview(${product.id})">

                                Submit Review

                            </button>

                        </div>


                        <!-- PRODUCT BUTTONS -->

                        <div class="product-buttons">

                            <div class="cart-buy-row">

                                <!-- ADD TO CART -->

                                <button
                                    type="button"
                                    class="add-cart-btn"
                                    ${outOfStock ? "disabled" : ""}
                                    onclick='addToCart(${JSON.stringify(product)})'>

                                    ${
                                        outOfStock
                                            ? "❌ Out of Stock"
                                            : "🛒 Add to Cart"
                                    }

                                </button>


                                <!-- BUY NOW -->

                                <button
                                    type="button"
                                    class="buy-now-btn"
                                    ${outOfStock ? "disabled" : ""}
                                    onclick='buyNow(${JSON.stringify(product)})'>

                                    ⚡ Buy Now

                                </button>

                            </div>


                            <!-- VIEW DETAILS -->

                            <a
                                href="product-details.html?id=${product.id}"
                                class="view-details-btn">

                                👁️ View Details

                            </a>

                        </div>

                    </div>

                `;


                productList.appendChild(card);


                /* LOAD REVIEWS */

                loadReviewsFromDatabase(
                    product.id
                );

            });

        }


        /* =========================================
           PRODUCT ICON
        ========================================= */

        function getProductIcon(name) {

            const productName =
                String(name || "")
                    .toLowerCase();

            if (
                productName.includes("keyboard")
            ) {
                return "⌨️";
            }

            if (
                productName.includes("headset") ||
                productName.includes("audio")
            ) {
                return "🎧";
            }

            if (
                productName.includes("mouse")
            ) {
                return "🖱️";
            }

            if (
                productName.includes("trigger")
            ) {
                return "🎮";
            }

            if (
                productName.includes("laptop")
            ) {
                return "💻";
            }

            if (
                productName.includes("phone") ||
                productName.includes("mobile")
            ) {
                return "📱";
            }

            return "🛍️";

        }


        /* =========================================
           HTML ESCAPE
        ========================================= */

        function escapeHtml(text) {

            const div =
                document.createElement("div");

            div.textContent =
                text || "";

            return div.innerHTML;

        }


        /* =========================================
           ADD TO CART
        ========================================= */

        function addToCart(product) {

            if (
                Number(product.quantity) <= 0
            ) {

                alert(
                    "❌ This product is out of stock."
                );

                return;

            }


            let cart =
                JSON.parse(
                    localStorage.getItem("cart")
                ) || [];


            let existingProduct =
                cart.find(
                    item =>
                        Number(item.id) ===
                        Number(product.id)
                );


            if (existingProduct) {

                existingProduct.quantity++;

            } else {

                cart.push({

                    id:
                        product.id,

                    name:
                        product.name,

                    price:
                        Number(product.price),

                    quantity:
                        1

                });

            }


            localStorage.setItem(
                "cart",
                JSON.stringify(cart)
            );


            alert(
                product.name +
                " added to cart!"
            );

        }


        /* =========================================
           BUY NOW
        ========================================= */

        function buyNow(product) {

            if (
                Number(product.quantity) <= 0
            ) {

                alert(
                    "❌ This product is out of stock."
                );

                return;

            }


            addToCart(product);


            window.location.href =
                "cart.html";

        }


        /* =========================================
           LOAD REVIEWS
        ========================================= */

        async function loadReviewsFromDatabase(
            productId
        ) {

            try {

                const response =
                    await fetch(
                        "/api/reviews/" +
                        productId
                    );


                if (!response.ok) {

                    throw new Error(
                        "Failed to load reviews"
                    );

                }


                const reviews =
                    await response.json();


                const reviewsBox =
                    document.getElementById(
                        "reviews-" +
                        productId
                    );


                const ratingInfo =
                    document.getElementById(
                        "rating-info-" +
                        productId
                    );


                if (!reviewsBox) {
                    return;
                }


                if (
                    !reviews ||
                    reviews.length === 0
                ) {

                    reviewsBox.innerHTML = `

                        <div class="no-reviews">
                            No customer reviews yet.
                        </div>

                    `;


                    if (ratingInfo) {

                        ratingInfo.innerText =
                            "No reviews yet";

                    }

                    return;

                }


                let totalRating = 0;


                reviews.forEach(review => {

                    totalRating +=
                        Number(
                            review.rating
                        );

                });


                const averageRating =
                    totalRating /
                    reviews.length;


                if (ratingInfo) {

                    ratingInfo.innerText =
                        "⭐ " +
                        averageRating.toFixed(1) +
                        "/5 • " +
                        reviews.length +
                        (
                            reviews.length === 1
                                ? " review"
                                : " reviews"
                        );

                }


                reviewsBox.innerHTML =
                    reviews.map(review => {

                        const rating =
                            Number(
                                review.rating
                            );


                        return `

                            <div class="customer-review">

                                <div class="review-stars">
                                    ${"★".repeat(rating)}
                                    ${"☆".repeat(5 - rating)}
                                </div>


                                <div class="review-author">

                                    <strong>
                                        ${escapeHtml(
                                            review.customerName
                                        )}
                                    </strong>

                                </div>


                                <div class="review-text">
                                    ${escapeHtml(
                                        review.reviewText
                                    )}
                                </div>


                                <div class="review-date">
                                    ${
                                        review.reviewDate ||
                                        ""
                                    }
                                </div>

                            </div>

                        `;

                    }).join("");


            } catch (error) {

                console.error(
                    "Error loading reviews:",
                    error
                );


                const ratingInfo =
                    document.getElementById(
                        "rating-info-" +
                        productId
                    );


                const reviewsBox =
                    document.getElementById(
                        "reviews-" +
                        productId
                    );


                if (ratingInfo) {

                    ratingInfo.innerText =
                        "Reviews unavailable";

                }


                if (reviewsBox) {

                    reviewsBox.innerHTML = `

                        <div class="no-reviews">
                            Unable to load reviews.
                        </div>

                    `;

                }

            }

        }


        /* =========================================
           SELECTED RATING
        ========================================= */

        function getSelectedRating(
            productId
        ) {

            return Number(
                localStorage.getItem(
                    "selectedRating_" +
                    productId
                )
            ) || 0;

        }


        /* =========================================
           RATING STARS
        ========================================= */

        function getRatingStars(
            productId
        ) {

            const selectedRating =
                getSelectedRating(
                    productId
                );


            let stars = "";


            for (
                let i = 1;
                i <= 5;
                i++
            ) {

                const filled =
                    i <= selectedRating;


                stars += `

                    <button
                        type="button"
                        class="rating-star ${
                            filled
                                ? "filled"
                                : ""
                        }"
                        onclick="setProductRating(
                            ${productId},
                            ${i}
                        )"
                        aria-label="Rate ${i} out of 5">

                        ${
                            filled
                                ? "★"
                                : "☆"
                        }

                    </button>

                `;

            }


            return stars;

        }


        /* =========================================
           SET PRODUCT RATING
        ========================================= */

        function setProductRating(
            productId,
            rating
        ) {

            localStorage.setItem(
                "selectedRating_" +
                productId,
                rating
            );


            const stars =
                document.getElementById(
                    "stars-" +
                    productId
                );


            if (stars) {

                stars.innerHTML =
                    getRatingStars(
                        productId
                    );

            }


            toggleReviewForm(
                productId,
                true
            );

        }


        /* =========================================
           TOGGLE REVIEW FORM
        ========================================= */

        function toggleReviewForm(
            productId,
            forceOpen = false
        ) {

            const form =
                document.getElementById(
                    "review-form-" +
                    productId
                );


            if (!form) {
                return;
            }


            if (
                forceOpen ||
                form.style.display !== "block"
            ) {

                form.style.display =
                    "block";

            } else {

                form.style.display =
                    "none";

            }

        }


        /* =========================================
           SUBMIT REVIEW
        ========================================= */

        async function submitReview(
            productId
        ) {

            const rating =
                getSelectedRating(
                    productId
                );


            const textElement =
                document.getElementById(
                    "review-text-" +
                    productId
                );


            const text =
                textElement
                    ? textElement.value.trim()
                    : "";


            if (
                rating < 1 ||
                rating > 5
            ) {

                alert(
                    "Please select a star rating first."
                );

                return;

            }


            if (!text) {

                alert(
                    "Please write a review."
                );


                if (textElement) {
                    textElement.focus();
                }

                return;

            }


            /* USER */

            const userName =
                localStorage.getItem(
                    "userName"
                );

            const userEmail =
                localStorage.getItem(
                    "userEmail"
                );


            const customerName =
                userName ||
                (
                    userEmail
                        ? userEmail.split("@")[0]
                        : ""
                );


            if (!customerName) {

                alert(
                    "🔐 Please login before submitting a review."
                );

                window.location.href =
                    "login.html";

                return;

            }


            try {

                const response =
                    await fetch(
                        "/api/reviews",
                        {

                            method: "POST",

                            headers: {
                                "Content-Type":
                                    "application/json"
                            },

                            body:
                                JSON.stringify({

                                    productId:
                                        productId,

                                    customerName:
                                        customerName.trim(),

                                    rating:
                                        rating,

                                    reviewText:
                                        text

                                })

                        }
                    );


                if (!response.ok) {

                    throw new Error(
                        "Review request failed"
                    );

                }


                const result =
                    await response.json();


                if (!result.success) {

                    alert(
                        result.message ||
                        "Failed to submit review."
                    );

                    return;

                }


                /* CLEAR RATING */

                localStorage.removeItem(
                    "selectedRating_" +
                    productId
                );


                /* CLEAR TEXT */

                if (textElement) {

                    textElement.value =
                        "";

                }


                /* CLOSE FORM */

                const form =
                    document.getElementById(
                        "review-form-" +
                        productId
                    );


                if (form) {

                    form.style.display =
                        "none";

                }


                /* RESET STARS */

                const stars =
                    document.getElementById(
                        "stars-" +
                        productId
                    );


                if (stars) {

                    stars.innerHTML =
                        getRatingStars(
                            productId
                        );

                }


                alert(
                    "Thank you! Your review has been submitted."
                );


                await loadReviewsFromDatabase(
                    productId
                );


            } catch (error) {

                console.error(
                    "Review submission error:",
                    error
                );


                alert(
                    "Unable to submit review. Please check the server."
                );

            }

        }


        /* =========================================
           CATEGORY FILTER
        ========================================= */

        function filterByCategory(
            category
        ) {

            const searchInput =
                document.getElementById(
                    "searchInput"
                );

            if (!searchInput) {
                return;
            }


            let keyword = "";


            if (category === "gaming") {
                keyword = "gaming";
            }

            if (category === "keyboard") {
                keyword = "keyboard";
            }

            if (category === "audio") {
                keyword = "headset";
            }

            if (category === "accessories") {
                keyword = "mouse";
            }

            if (category === "electronics") {
                keyword = "";
            }


            searchInput.value =
                keyword;


            filterProducts();

        }


        /* =========================================
           DEALS
        ========================================= */

        function showDeals() {

            const productList =
                document.getElementById(
                    "productList"
                );

            if (!productList) {
                return;
            }


            const message =
                document.getElementById(
                    "message"
                );


            const availableProducts =
                allProducts.filter(
                    product =>
                        Number(product.quantity) > 0
                );


            productList.innerHTML = "";


            if (
                availableProducts.length === 0
            ) {

                message.innerText =
                    "No deals available.";

                return;

            }


            message.innerText =
                "🔥 Showing available deals";


            availableProducts.forEach(
                product => {

                    const searchInput =
                        document.getElementById(
                            "searchInput"
                        );

                    searchInput.value =
                        "";


                }
            );


            filterProducts();

            scrollToProducts();

        }


        /* =========================================
           TOP RATED
        ========================================= */

        async function showTopRated() {

            scrollToProducts();

            alert(
                "⭐ Product ratings are displayed on each product card."
            );

        }


        /* =========================================
           SCROLL PRODUCTS
        ========================================= */

        function scrollToProducts() {

            const products =
                document.getElementById(
                    "products"
                );

            if (products) {

                products.scrollIntoView({
                    behavior: "smooth"
                });

            }

        }


        /* =========================================
           CHATBOT OPEN / CLOSE
        ========================================= */

        function toggleChatbot() {

            const chatbot =
                document.getElementById(
                    "chatbotBox"
                );


            if (
                chatbot.style.display ===
                "block"
            ) {

                chatbot.style.display =
                    "none";

            } else {

                chatbot.style.display =
                    "block";


                const input =
                    document.getElementById(
                        "chatInput"
                    );


                if (input) {
                    input.focus();
                }

            }

        }


        /* =========================================
           ADD CHAT MESSAGE
        ========================================= */

        function addChatMessage(
            message,
            type
        ) {

            const messages =
                document.getElementById(
                    "chatMessages"
                );


            const div =
                document.createElement(
                    "div"
                );


            div.className =
                "chat-message " +
                (
                    type === "user"
                        ? "user-message"
                        : "bot-message"
                );


            div.innerText =
                message;


            messages.appendChild(
                div
            );


            messages.scrollTop =
                messages.scrollHeight;

        }


        /* =========================================
           SEND CHAT MESSAGE
        ========================================= */

        async function sendChatMessage() {

            const input =
                document.getElementById(
                    "chatInput"
                );


            const message =
                input.value
                    .trim();


            if (!message) {
                return;
            }


            addChatMessage(
                message,
                "user"
            );


            input.value = "";


            try {

                const response =
                    await fetch(
                        "/api/chatbot",
                        {

                            method: "POST",

                            headers: {
                                "Content-Type":
                                    "application/json"
                            },

                            body:
                                JSON.stringify({
                                    message:
                                        message
                                })

                        }
                    );


                if (!response.ok) {

                    throw new Error(
                        "Chatbot request failed"
                    );

                }


                const data =
                    await response.json();


                addChatMessage(
                    data.reply,
                    "bot"
                );


            } catch (error) {

                console.error(
                    "Chatbot Error:",
                    error
                );


                addChatMessage(
                    "❌ Sorry, chatbot server is unavailable.",
                    "bot"
                );

            }

        }


        /* =========================================
           ENTER KEY CHATBOT
        ========================================= */

        function handleChatKey(event) {

            if (
                event.key === "Enter"
            ) {

                sendChatMessage();

            }

        }


        /* =========================================
           PAGE LOAD
        ========================================= */

        window.onload =
            function () {

                updateUserSection();

                loadProducts();

            };

    </script>

</body>

</html>