"use strict";

function _classPrivateFieldLooseBase(receiver, privateKey) { if (!Object.prototype.hasOwnProperty.call(receiver, privateKey)) { throw new TypeError("attempted to use private field on non-instance"); } return receiver; }

var id = 0;

function _classPrivateFieldLooseKey(name) { return "__private_" + id++ + "_" + name; }

var _publish = /*#__PURE__*/_classPrivateFieldLooseKey("publish");

/**
 * Default store that keeps state in a simple object.
 */
class DefaultStore {
  constructor() {
    Object.defineProperty(this, _publish, {
      value: _publish2
    });
    this.state = {};
    this.callbacks = [];
  }

  getState() {
    return this.state;
  }

  setState(patch) {
    const prevState = { ...this.state
    };
    const nextState = { ...this.state,
      ...patch
    };
    this.state = nextState;

    _classPrivateFieldLooseBase(this, _publish)[_publish](prevState, nextState, patch);
  }

  subscribe(listener) {
    this.callbacks.push(listener);
    return () => {
      // Remove the listener.
      this.callbacks.splice(this.callbacks.indexOf(listener), 1);
    };
  }

}

function _publish2() {
  for (var _len = arguments.length, args = new Array(_len), _key = 0; _key < _len; _key++) {
    args[_key] = arguments[_key];
  }

  this.callbacks.forEach(listener => {
    listener(...args);
  });
}

DefaultStore.VERSION = "2.0.3";

module.exports = function defaultStore() {
  return new DefaultStore();
};