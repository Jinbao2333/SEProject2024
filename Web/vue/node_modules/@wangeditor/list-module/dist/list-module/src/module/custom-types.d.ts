/**
 * @description list element
 * @author wangfupeng
 */
import { Text } from 'slate';
export declare type ListItemElement = {
    type: 'list-item';
    children: Text[];
};
export declare type NumberedListElement = {
    type: 'numbered-list';
    children: ListItemElement[];
};
export declare type BulletedListElement = {
    type: 'bulleted-list';
    children: ListItemElement[];
};
