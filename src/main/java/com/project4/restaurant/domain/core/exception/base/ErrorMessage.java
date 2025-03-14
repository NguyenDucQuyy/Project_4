package com.project4.restaurant.domain.core.exception.base;

public class ErrorMessage {
  public static final String INTERNAL_SERVER = "internal.service";
  public static final String JWT_INVALID = "jwt.invalid";
  public static final String TOKEN_INVALID = "token.invalid";
  //==================================================tai khoan================================
  public static final String USER_DELETED = "user.deleted";
  public static final String PARTNER_AUTH_ERROR = "partner.auth.error";
  //============================================================================================
  public static final String GOODS_NOT_FOUND = "goods.not.found";
  public static final String VEHICLE_LOAD_CAPACITY_NOT_FOUND = "vehicle.load.capacity.not.found";
  //================================================Thong tin to khai hai quan ==================
  public static final String CUSTOMS_REGISTRATION_NOT_FOUND = "customs.registration.not.found";
  public static final String COMPANY_TAX_CODE_NOT_MATCH = "company.tax.code.not.match";
  //================================================ BOOKING ====================================
  public static final String BOOKING_PAYMENT_INVALID = "booking.payment.invalid";
  public static final String BOOKING_PAYMENT_SERVICE_NOT_EXISTS = "booking.payment.service.not.exists";
  public static final String BOOKING_CURRENT_PAYMENT = "booking.current.payment";
  public static final String BOOKING_CURRENT_UPDATED = "booking.current.updated";
  public static final String BOOKING_ID_NOT_EMPTY = "booking.id.not.empty";
  public static final String BOOKING_NOT_FOUND = "booking.not.found";
  public static final String BOOKING_REGISTRATION_NOT_EMPTY = "booking.registration.not.empty";
  public static final String BOOKING_HAD_BEEN_SENT = "booking.had.been.sent";
  public static final String BOOKING_INFO_NOT_EMPTY = "booking.info.not.empty";
  public static final String BOOKING_STEP_STATE_NOT_EMPTY = "booking.step.state.not.found";
  public static final String FEE_PAYING_COMPANY_TAX_CODE_NOT_EMPTY = "fee.paying.company.tax.code.not.empty";
  public static final String FEE_PAYING_COMPANY_TAX_CODE_FROM_TO_CHARACTERS = "fee.paying.company.tax.code.from.to.characters";
  public static final String FEE_PAYING_COMPANY_TAX_CODE_NOT_CONTAIN_SPACES = "fee.paying.company.tax.code.not.contain.spaces";
  public static final String FEE_PAYING_COMPANY_TAX_CODE_CANNOT_ENTER_SPECIAL_CHARACTERS = "fee.paying.company.tax.code.cannot.enter.special.characters";

  public static final String FEE_PAYING_COMPANY_NAME_NOT_EMPTY = "fee.paying.company.name.not.empty";
  public static final String FEE_PAYING_COMPANY_NAME_MUST_NOT_EXCEED_CHARACTERS = "fee.paying.company.name.must.not.exceed.characters";

  public static final String FEE_PAYING_COMPANY_ADDRESS_MUST_NOT_EXCEED_CHARACTERS = "fee.paying.company.address.must.not.exceed.characters";

  public static final String FEE_PAYING_COMPANY_PHONE_NUMBER_NOT_INVALID = "fee.paying.company.phone.number.not.valid";
  public static final String FEE_PAYING_COMPANY_PHONE_NUMBER_FROM_TO_CHARACTERS = "fee.paying.company.phone.number.from.to.characters";

  public static final String BOOKING_IMPORT_EXPORT_SHIPMENT_TYPE_NOT_MATCH = "booking.import.export.shipment.type.not.match";
  public static final String BOOKING_COMMODITY_CATEGORY_CODE_NOT_MATCH = "booking.commodity.category.code.not.match";
  public static final String BOOKING_COMMODITY_CATEGORY_NAME_NOT_MATCH = "booking.commodity.category.name.not.match";

  public static final String BOOKING_GENERAL_INFO_NOT_CHANGE = "booking.general.info.cannot.change";
  public static final String BOOKING_VEHICLE_TRANSPORT_NOT_CHANGE = "booking.vehicle.transport.cannot.change";
  public static final String BOOKING_VEHICLE_LOAD_TRANSFER_NOT_CHANGE = "booking.vehicle.load.transfer.cannot.change";
  public static final String BOOKING_VEHICLE_PAID_DELETE = "booking.vehicle.paid.delete";

  public static final String ARRIVAL_DATE_CANNOT_EMPTY = "arrival.date.cannot.empty";
  public static final String ARRIVAL_DATE_NOT_VALID = "arrival.date.not.valid";

  public static final String ARRIVAL_DATE_AFTER_DATE = "arrival.date.after.date";

  public static final String ARRIVAL_DATE_BEFORE_DATE = "arrival.date.before.date";
  public static final String ARRIVAL_TIME_NOT_VALID = "arrival.time.not.valid";
  public static final String ARRIVAL_TIME_BEFORE_TIME = "arrival.time.before.time";

  public static final String CUSTOMS_CLEARANCE_AREA_NOT_EMPTY = "customs.clearance.area.not.empty";

  public static final String IMPORT_EXPORT_SHIPMENT_TYPE_NOT_EMPTY = "import.export.shipment.type.not.empty";
  public static final String IMPORT_EXPORT_SHIPMENT_TYPE_NOT_FOUND = "import.export.shipment.type.not.found";

  public static final String GOODS_TYPE_NOT_EMPTY = "goods.type.not.empty";
  public static final String GOODS_TYPE_CODE_NOT_EMPTY = "goods.type.code.not.empty";
  public static final String GOODS_TYPE_NAME_NOT_EMPTY = "goods.type.name.not.empty";

  public static final String IS_E_COMMERCE_GOODS_NOT_EMPTY = "is.ecommerce.goods.not.empty";

  public static final String PROCEDURE_OFFICER_NAME_MUST_NOT_EXCEED_CHARACTERS = "procedure.officer.name.must.not.exceed.characters";

  public static final String PROCEDURE_OFFICER_PHONE_NUMBER_NOT_VALID = "procedure.officer.phone.number.not.valid";

  public static final String PROCEDURE_OFFICER_PHONE_NUMBER_FROM_TO_CHARACTERS = "procedure.officer.phone.number.from.to.characters";

  //thong tin xe
  public static final String BOOKING_VEHICLE_EXISTS = "booking.vehicle.exists";

  public static final String BOOKING_VEHICLE_MAX_SIZE = "booking.vehicle.max.size";

  public static final String BOOKING_VEHICLE_USED = "booking.vehicle.used";
  public static final String BOOKING_VEHICLE_TRANSPORT_NOT_EMPTY = "booking.vehicle.transport.not.empty";

  public static final String BOOKING_VEHICLE_DRIVER_PHONE_NUMBER_DUPLICATE = "booking.vehicle.driver.phone.number.duplicate";

  public static final String BOOKING_VEHICLE_LICENSE_PLATE_DUPLICATE = "booking.vehicle.license.plate.duplicate";

  public static final String BOOKING_VEHICLE_TRANSPORT_LICENSE_PLATE_NOT_EMPTY = "booking.vehicle.transport.license.plate.not.empty";
  public static final String BOOKING_VEHICLE_TRANSPORT_LICENSE_PLATE_EXCEED_CHARACTERS = "booking.vehicle.transport.license.plate.exceed.characters";

  public static final String BOOKING_VEHICLE_TRANSPORT_TYPE_NOT_EMPTY = "booking.vehicle.transport.type.not.empty";

  public static final String BOOKING_VEHICLE_TRANSPORT_MOOC_NUMBER_NOT_CONTAIN_SPACES = "booking.vehicle.transport.mooc.number.not.contain.spaces";
  public static final String BOOKING_VEHICLE_TRANSPORT_MOOC_NUMBER_EXCEED_CHARACTERS = "booking.vehicle.transport.mooc.number.exceed.characters";

  public static final String BOOKING_VEHICLE_TRANSPORT_CONTAINER_NUMBER_EXCEED_CHARACTERS = "booking.vehicle.transport.container.number.exceed.characters";
  public static final String BOOKING_VEHICLE_TRANSPORT_CONTAINER_NUMBER_NOT_CONTAIN_SPACES = "booking.vehicle.transport.container.number.not.contain.spaces";

  public static final String BOOKING_VEHICLE_TRANSPORT_DRIVER_PHONE_NUMBER_NOT_EMPTY = "booking.vehicle.transport.driver.phone.number.not.empty";
  public static final String BOOKING_VEHICLE_TRANSPORT_DRIVER_PHONE_NUMBER_FROM_TO_CHARACTERS = "booking.vehicle.transport.driver.phone.number.from.to.characters";
  public static final String BOOKING_VEHICLE_TRANSPORT_DRIVER_PHONE_NUMBER_NOT_VALID = "booking.vehicle.transport.driver.phone.number.not.valid";

  public static final String BOOKING_VEHICLE_TRANSPORT_DRIVER_NAME_NOT_EMPTY = "booking.vehicle.transport.driver.name.not.empty";
  public static final String BOOKING_VEHICLE_TRANSPORT_DRIVER_NAME_EXCEED_CHARACTERS = "booking.vehicle.transport.driver.name.exceed.characters";

  public static final String BOOKING_VEHICLE_TRANSPORT_COMPANION_NAME_EXCEED_CHARACTERS = "booking.vehicle.transport.companion.name.exceed.characters";

  public static final String BOOKING_VEHICLE_TRANSPORT_DRIVER_MSISDN_NOT_EMPTY = "booking.vehicle.transport.driver.msisdn.not.empty";
  public static final String BOOKING_VEHICLE_TRANSPORT_DRIVER_MSISDN_EXCEED_CHARACTERS = "booking.vehicle.transport.driver.msisdn.exceed.characters";
  public static final String BOOKING_VEHICLE_TRANSPORT_DRIVER_MSISDN_NOT_CONTAIN_SPACES = "booking.vehicle.transport.driver.msisdn.not.contain.spaces";

  public static final String BOOKING_VEHICLE_TRANSPORT_COME_FROM_CANNOT_ENTER_SPECIAL_CHARACTERS = "booking.vehicle.transport.vehicle.come.from.cannot.enter.special.characters";

  public static final String BOOKING_VEHICLE_TRANSPORT_SEAL_EXCEED_CHARACTERS = "booking.vehicle.transport.vehicle.seal.exceed.characters";
  public static final String BOOKING_VEHICLE_TRANSPORT_SEAL_NOT_CONTAIN_SPACES = "booking.vehicle.transport.vehicle.seal.not.contain.spaces";

  public static final String BOOKING_VEHICLE_TRANSPORT_LOAD_DUE_TO_WEIGHT_NOT_EMPTY = "booking.vehicle.transport.vehicle.load.due.to.own.weight.not.empty";
  public static final String BOOKING_VEHICLE_TRANSPORT_LOAD_DUE_TO_WEIGHT_POSITIVE = "booking.vehicle.transport.vehicle.load.due.to.own.weight.positive";

  public static final String BOOKING_VEHICLE_TRANSPORT_PRODUCT_WEIGHT_NOT_EMPTY = "booking.vehicle.transport.vehicle.product.weight.not.empty";
  public static final String BOOKING_VEHICLE_TRANSPORT_PRODUCT_WEIGHT_POSITIVE = "booking.vehicle.transport.vehicle.product.weight.positive";

  public static final String BOOKING_VEHICLE_TRANSPORT_LOAD_CAPACITY_NOT_EMPTY = "booking.vehicle.transport.vehicle.load.capacity.not.empty";

  public static final String BOOKING_VEHICLE_TRANSPORT_GOODS_NOT_EMPTY = "booking.vehicle.transport.good.not.empty";

  public static final String BOOKING_VEHICLE_TRANSPORT_NOTE_EXCEED_CHARACTERS = "booking.vehicle.transport.vehicle.note.exceed.characters";

  public static final String BOOKING_VEHICLE_TRANSPORT_LOAD_TRANSFER_NOT_EMPTY = "booking.vehicle.transport.vehicle.load.transfer.not.empty";


  public static final String BOOKING_VEHICLE_LOAD_TRANSFER_NOT_EMPTY = "booking.vehicle.load.transfer.not.empty";

  public static final String BOOKING_VEHICLE_LOAD_TRANSFER_LICENSE_PLATE_NOT_EMPTY = "booking.vehicle.load.transfer.license.plate.not.empty";
  public static final String BOOKING_VEHICLE_LOAD_TRANSFER_LICENSE_PLATE_EXCEED_CHARACTERS = "booking.vehicle.load.transfer.license.plate.exceed.characters";

  public static final String BOOKING_VEHICLE_LOAD_TRANSFER_TYPE_NOT_EMPTY = "booking.vehicle.load.transfer.type.not.empty";

  public static final String BOOKING_VEHICLE_LOAD_TRANSFER_MOOC_NUMBER_NOT_CONTAIN_SPACES = "booking.vehicle.load.transfer.mooc.number.not.contain.spaces";
  public static final String BOOKING_VEHICLE_LOAD_TRANSFER_MOOC_NUMBER_EXCEED_CHARACTERS = "booking.vehicle.load.transfer.mooc.number.exceed.characters";

  public static final String BOOKING_VEHICLE_LOAD_TRANSFER_CONTAINER_NUMBER_EXCEED_CHARACTERS = "booking.vehicle.load.transfer.container.number.exceed.characters";
  public static final String BOOKING_VEHICLE_LOAD_TRANSFER_CONTAINER_NUMBER_NOT_CONTAIN_SPACES = "booking.vehicle.load.transfer.container.number.not.contain.spaces";

  //dich vu
  public static final String BOOKING_SERVICE_LICENSE_PLATE_NOT_EMPTY = "booking.service.license.plate.not.empty";
  public static final String BOOKING_SERVICE_LICENSE_PLATE_EXCEED_CHARACTERS = "booking.service.license.plate.exceed.characters";
  public static final String BOOKING_SERVICE_VEHICLE_PRICE_POSITIVE = "booking.service.vehicle.price.positive";
  public static final String BOOKING_SERVICE_TRANSHIPMENT_TYPE_NOT_EMPTY = "booking.service.transhipment.type.not.empty";
  public static final String BOOKING_SERVICE_TRANSHIPMENT_NOT_SELECTED = "booking.service.transhipment.not.selected";
  public static final String BOOKING_SERVICE_TRANSHIPMENT_CANNOT_SELECTED = "booking.service.transhipment.cannot.selected";
  public static final String BOOKING_SERVICE_LICENSE_PLATE_NOT_EXIST = "booking.service.license.plate.not.exist";
  public static final String BOOKING_SERVICE_BALE_ATTACH_FILE_NOT_EMPTY = "booking.service.bale.attach.file.not.empty";
  public static final String BOOKING_SERVICE_NOT_PRICING_TYPE = "booking.service.not.pricing.type";
  public static final String BOOKING_SERVICE_NOT_ENABLE = "booking.service.not.enable";
  public static final String BOOKING_SERVICE_NOT_CHANGE = "booking.service.cannot.change";
  public static final String BOOKING_SERVICE_EXISTS = "booking.service.exists";
  public static final String BOOKING_SERVICE_NOT_EXISTS = "booking.service.not.exists";
  public static final String BOOKING_SERVICE_VEHICLE_NOT_EXISTS = "booking.service.vehicle.not.exists";
  public static final String BOOKING_SERVICE_TRANSHIPMENT_VEHICLE_NOT_EMPTY = "booking.service.transhipment.vehicle.not.empty";
  public static final String BOOKING_SERVICE_NUMBER_OF_DAYS_IN_STORAGE_CANNOT_MODIFIED = "booking.service.number.of.days.in.storage.cannot.modified";
  public static final String BOOKING_SERVICE_UNSELECT_PAID = "booking.service.unselect.paid";
  public static final String BOOKING_SERVICE_VEHICLE_PAID_UNSELECT = "booking.service.vehicle.paid.unselect";
  public static final String BOOKING_SERVICE_VEHICLE_PAYMENT_STATE_LICENSE_PLATE_NOT_CHANGE = "booking.service.vehicle.payment.state.license.plate.not.change";
  public static final String BOOKING_SERVICE_VEHICLE_PAYMENT_STATE_DELETE = "booking.service.vehicle.payment.state.delete";
  public static final String BOOKING_SERVICE_VEHICLE_IN_SERVICE_PAYMENT_UPDATE = "booking.service.vehicle.in.service.payment.update";

  public static final String BOOKING_SERVICE_DELETED = "booking.service.deleted";

  public static final String BOOKING_SERVICE_PAID = "booking.service.paid";

  public static final String BOOKING_SERVICE_VEHICLE_DELETED = "booking.service.vehicle.deleted";

  public static final String BOOKING_SERVICE_VEHICLE_PAID = "booking.service.vehicle.paid";

  public static final String BOOKING_CUSTOMS_DECLARATION_CANNOT_CHANGE = "booking.customs.declaration.cannot.change";

  //
  public static final String BOOKING_VEHICLE_NOT_FOUND = "booking.vehicle.not.found";
  public static final String BOOKING_VEHICLE_RECEIVED = "booking.vehicle.received";
  public static final String BOOKING_VEHICLE_NOT_RECEIVED = "booking.vehicle.not.received";
  public static final String BOOKING_VEHICLE_STARTED = "booking.vehicle.started";

  //
  public static final String BOOKING_REGISTRATION_TRANSPORT_GOODS_EXISTS = "booking.registration.transport.goods.exits";

  public static final String BOOKING_PAYMENT_PRICING_EMPTY = "booking.payment.pricing.empty";
  public static final String BOOKING_SERVICE_PARTNER_EMPTY = "booking.service.partner.empty";
  public static final String BOOKING_SERVICE_VEHICLE_PRICING_EMPTY = "booking.service.vehicle.pricing.empty";

  public static final String PAYMENT_LIST_BOOKING_NOT_EMPTY = "payment.list.booking.not.empty";
  public static final String PAYMENT_METHOD_NOT_EMPTY = "payment.method.not.empty";


  public static final String IMPORT_FILE_EXCEL_INVALID = "import.file.excel.invalid";

  public static final String DATE_FROM_NOT_EMPTY = "date.from.not.empty";
  public static final String DATE_TO_NOT_EMPTY = "date.to.not.empty";
  public static final String DATE_FROM_BEFORE_DATE_TO = "date.from.before.date.to";

  public static final String PAYMENT_HISTORY_NOT_FOUND = "payment.history.not.found";

  public static final String DATE_FROM_DATE_TO_MAX = "date.from.date.to.max";
}
