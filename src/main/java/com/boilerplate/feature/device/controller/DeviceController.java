package com.boilerplate.feature.device.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class DeviceController {
    @GetMapping
    public String test() {
        return "Only admin can access /api/devices";
    }
}
