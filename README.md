Product Detection – On-Camera AR (Android)
Overview

This project implements a real-time product detection system using on-camera augmented reality (AR).
The app continuously scans retail shelves, detects visible products, and overlays bounding boxes and tick marks directly on the live camera feed.

The solution is designed to meet the assignment requirements of live detection, continuous scanning, and non-duplication of detected products.

Key Features

Live Camera Scanning using CameraX

Real-time Object Detection using ML Kit (STREAM_MODE)

On-Camera AR Overlay (bounding boxes + tick marks)

Continuous Scanning as the user pans the camera

Non-Duplication Logic to avoid re-detecting already scanned products

Technical Approach

CameraX ImageAnalysis is used for frame-by-frame live processing.

ML Kit Object Detection detects products in real time.

A custom tracking mechanism ensures persistence:

Products are tracked using IOU + centroid distance

A product is confirmed only after being seen in multiple consecutive frames

Once confirmed, a product is never re-detected, even if the camera pans away and returns

Jetpack Compose Canvas is used to draw AR overlays directly on the camera preview.
