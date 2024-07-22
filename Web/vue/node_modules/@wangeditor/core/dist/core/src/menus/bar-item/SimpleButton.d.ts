/**
 * @description button class
 * @author wangfupeng
 */
import { IButtonMenu } from '../interface';
import BaseButton from './BaseButton';
declare class SimpleButton extends BaseButton {
    constructor(menu: IButtonMenu, inGroup?: boolean);
    onButtonClick(): void;
}
export default SimpleButton;
