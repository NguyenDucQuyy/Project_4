package com.project4.restaurant.domain.entity.type;

public enum AccountRole {
  ADMIN("ADMIN"),
  MANAGER("Quản lý nhà hàng"),
  STAFF("Nhân viên nhà hàng"),
  SHIPPER("Shipper"),
  CUSTOMER("Khách hàng");

  private final String displayName;

  AccountRole(String displayName) {
    this.displayName = displayName;
  }

  @Override
  public String toString() {
    return displayName;
  }
}
