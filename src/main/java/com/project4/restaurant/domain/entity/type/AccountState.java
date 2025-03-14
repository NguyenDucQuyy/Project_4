package com.project4.restaurant.domain.entity.type;

public enum AccountState {
  ACTIVE("Hoạt động"),
  INACTIVE("Không hoạt động"),
  DELETED("Đã xóa");

  private final String displayName;

  AccountState(String displayName) {
    this.displayName = displayName;
  }

  @Override
  public String toString() {
    return displayName;
  }
}
